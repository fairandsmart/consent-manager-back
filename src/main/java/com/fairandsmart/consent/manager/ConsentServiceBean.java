package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.util.SortUtil;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.manager.model.Treatment;
import com.fairandsmart.consent.manager.store.ReceiptAlreadyExistsException;
import com.fairandsmart.consent.manager.store.ReceiptStore;
import com.fairandsmart.consent.manager.store.ReceiptStoreException;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.time.Instant;
import java.time.Period;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.fairandsmart.consent.manager.entity.ModelEntry.DEFAULT_BRANCHE;

@ApplicationScoped
public class ConsentServiceBean implements ConsentService {

    private static final Logger LOGGER = Logger.getLogger(ConsentService.class.getName());

    @Inject
    AuthenticationService authentication;

    @Inject
    SerialGenerator generator;

    @Inject
    TokenService tokenService;

    @Inject
    ReceiptStore store;

    @ConfigProperty(name = "consent.processor")
    String processor;

    /* MODELS MANAGEMENT */

    @Override
    public CollectionPage<ModelEntry> listEntries(ModelFilter filter) {
        LOGGER.log(Level.INFO, "Listing models entries");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        PanacheQuery<ModelEntry> query;
        Sort sort = SortUtil.fromFilter(filter);
        if (sort != null) {
            query = ModelEntry.find("owner = ?1 and type in ?2", sort, connectedIdentifier, filter.getTypes());
        } else {
            query = ModelEntry.find("owner = ?1 and type in ?2", connectedIdentifier, filter.getTypes());
        }
        CollectionPage<ModelEntry> result = new CollectionPage<>();
        result.setValues(query.page(Page.of(filter.getPage(), filter.getSize())).list());
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(query.count());
        return result;
    }

    @Override
    @Transactional
    public ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "Creating new entry");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        if ( ModelEntry.isKeyAlreadyExistsForOwner(connectedIdentifier, key)) {
            throw new EntityAlreadyExistsException("A model entry already exists with key: " + key);
        }
        ModelEntry entry = new ModelEntry();
        entry.type = type;
        entry.key = key;
        entry.name = name;
        entry.description = description;
        entry.branches = DEFAULT_BRANCHE;
        entry.owner = connectedIdentifier;
        entry.persist();
        return entry;
    }

    @Override
    public ModelEntry getEntry(String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Getting entry for id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelEntry> optional = ModelEntry.findByIdOptional(id);
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + id));
        if ( !entry.owner.equals(connectedIdentifier) ) {
            throw new AccessDeniedException("access denied to version with id: " + id);
        }
        return entry;
    }

    @Override
    public ModelEntry findEntryForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding entry for key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelEntry> optional = ModelEntry.find("owner = ?1 and key = ?2", connectedIdentifier, key).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for key: " + key));
    }

    @Override
    @Transactional
    public ModelEntry updateEntry(String id, String name, String description) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating entry for id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelEntry> optional = ModelEntry.find("id = ?1 and owner = ?2", id, connectedIdentifier).singleResultOptional();
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + id));
        entry.name = name;
        entry.description = description;
        entry.persist();
        return entry;
    }

    @Override
    @Transactional
    public void deleteEntry(String id) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Deleting entry with id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.id = ?2", connectedIdentifier, id).list();
        if ( versions.isEmpty() || versions.stream().allMatch(v -> v.status.equals(ModelVersion.Status.DRAFT))) {
            ModelEntry.deleteById(id);
        } else {
            //TODO Maybe allow this but ensure that all corresponding records are going to be deleted... and that receipt may be corrupted.
            throw new ConsentManagerException("unable to delete entry that have not only DRAFT versions");
        }
    }

    @Override
    @Transactional
    public ModelVersion createVersion(String entryId, String locale, ModelData data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Creating new version for entry with id: " + entryId);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelEntry> optional = ModelEntry.find("id = ?1 and owner = ?2", entryId, connectedIdentifier).singleResultOptional();
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + entryId));
        if ( !entry.type.equals(data.getType()) ) {
            throw new ConsentManagerException("Entry data type mismatch, need type: " + entry.type);
        }
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and entry.id = ?2 and child = ?3", connectedIdentifier, entryId, "").singleResultOptional();
        ModelVersion latest = voptional.orElse(null);
        try {
            long now = System.currentTimeMillis();
            if (latest == null) {
                LOGGER.log(Level.INFO, "No existing version found, creating first one");
                latest = new ModelVersion();
                latest.entry = entry;
                latest.owner = connectedIdentifier;
                latest.branches = DEFAULT_BRANCHE;
                latest.creationDate = now;
                latest.status = ModelVersion.Status.DRAFT;
                latest.serial = generator.next(ModelVersion.class.getName());
                latest.defaultLocale = locale;
                latest.type = ModelVersion.Type.MINOR;
            } else if (latest.status.equals(ModelVersion.Status.DRAFT)) {
                throw new ConsentManagerException("A draft version already exists, unable to create new one");
            } else {
                LOGGER.log(Level.INFO, "Latest version found, creating new one");
                ModelVersion newversion = new ModelVersion();
                newversion.entry = latest.entry;
                newversion.owner = connectedIdentifier;
                newversion.branches = DEFAULT_BRANCHE;
                newversion.creationDate = now;
                newversion.status = ModelVersion.Status.DRAFT;
                newversion.serial = generator.next(ModelVersion.class.getName());
                newversion.parent = latest.serial;
                newversion.defaultLocale = latest.defaultLocale;
                newversion.availableLocales = latest.availableLocales;
                newversion.content = latest.content;
                newversion.counterparts = latest.counterparts;
                newversion.type = ModelVersion.Type.MINOR;
                newversion.addCounterpart(latest.serial);

                latest.child = newversion.serial;
                latest.persist();
                latest = newversion;
            }
            latest.addAvailableLocale(locale);
            latest.content.put(locale, new ModelContent().withDataObject(data).withAuthor(connectedIdentifier));
            latest.modificationDate = now;
            latest.persist();
            return latest;
        } catch ( SerialGeneratorException ex ) {
            throw new ConsentManagerException("unable to generate serial number for new version", ex);
        } catch ( ModelDataSerializationException ex ) {
            throw new ConsentManagerException("unable to serialise data", ex);
        }
    }

    @Override
    public ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding active version for entry with key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        return systemFindActiveVersionByKey(connectedIdentifier, key);
    }

    @Override
    public ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding active version for entry with id: " + entryId);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        return systemFindActiveVersionByEntryId(connectedIdentifier, entryId);
    }

    @Override
    public ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding latest version for entry with key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and entry.key = ?2 and child = ?3", connectedIdentifier, key, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with key: " + key));
    }

    @Override
    public ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding latest version for entry with id: " + entryId);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and entry.id = ?2 and child = ?3", connectedIdentifier, entryId, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with id: " + entryId));
    }

    @Override
    public ModelVersion getVersion(String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Finding version for id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> optional = ModelVersion.findByIdOptional(id);
        ModelVersion version = optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for id: " + id));
        if ( !version.owner.equals(connectedIdentifier) ) {
            throw new AccessDeniedException("access denied to version with id: " + id);
        }
        return version;
    }

    @Override
    public ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding version for serial: " + serial);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> optional = ModelVersion.find("owner = ?1 and serial = ?2", connectedIdentifier, serial).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for serial: " + serial));
    }

    @Override
    public List<ModelVersion> getVersionHistoryForKey(String key) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Listing versions for entry with key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.key = ?2", connectedIdentifier, key).list();
        if ( !versions.isEmpty() ) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    public List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Listing versions for entry with id: " + entryId);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.id = ?2", connectedIdentifier, entryId).list();
        if ( !versions.isEmpty() ) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    @Transactional
    public ModelVersion updateVersion(String id, String locale, ModelData data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating content for version with id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", connectedIdentifier, id).singleResultOptional();
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if ( !version.entry.type.equals(data.getType()) ) {
            throw new ConsentManagerException("Entry data type mismatch, need type: " + version.entry.type);
        }
        if ( !version.child.isEmpty() ) {
            throw new ConsentManagerException("Unable to update content for version that is not last one");
        }
        try {
            version.addAvailableLocale(locale);
            version.content.put(locale, new ModelContent().withDataObject(data).withAuthor(connectedIdentifier));
            version.modificationDate = System.currentTimeMillis();
            version.persist();
            return version;
        } catch ( ModelDataSerializationException ex ) {
            throw new ConsentManagerException("unable to serialise data", ex);
        }
    }

    @Override
    @Transactional
    public ModelVersion updateVersionType(String id, ModelVersion.Type type) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating type for version with id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", connectedIdentifier, id).singleResultOptional();
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if ( !version.child.isEmpty() ) {
            throw new ConsentManagerException("Unable to update type for version that is not last one");
        }
        if ( !version.status.equals(ModelVersion.Status.DRAFT) ) {
            throw new ConsentManagerException("Unable to update type for version that is not DRAFT");
        }
        version.type = type;
        version.modificationDate = System.currentTimeMillis();
        version.persist();
        return version;
    }

    @Override
    @Transactional
    public ModelVersion updateVersionStatus(String id, ModelVersion.Status status) throws ConsentManagerException, EntityNotFoundException, InvalidStatusException {
        LOGGER.log(Level.INFO, "Updating status for version with id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", connectedIdentifier, id).singleResultOptional();
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if ( !version.child.isEmpty() ) {
            throw new ConsentManagerException("Unable to update status of a version that is not the latest");
        }
        if ( status.equals(ModelVersion.Status.DRAFT) ) {
            throw new InvalidStatusException("Unable to update a version to DRAFT status");
        }
        if ( status.equals(ModelVersion.Status.ACTIVE) ) {
            if ( !version.status.equals(ModelVersion.Status.DRAFT) ) {
                throw new InvalidStatusException("Only DRAFT version can be set ACTIVE");
            } else {
                if ( !version.parent.isEmpty() ) {
                    ModelVersion parent = ModelVersion.findById(version.parent);
                    parent.modificationDate = System.currentTimeMillis();
                    parent.status = ModelVersion.Status.ARCHIVED;
                    parent.persist();
                }
                version.status = ModelVersion.Status.ACTIVE;
                version.modificationDate = System.currentTimeMillis();
                if ( version.type.equals(ModelVersion.Type.MAJOR) ) {
                    version.counterparts = "";
                }
                version.persist();
            }
        }
        if ( status.equals(ModelVersion.Status.ARCHIVED) ) {
            if ( !version.status.equals(ModelVersion.Status.ACTIVE) ) {
                throw new InvalidStatusException("Only ACTIVE version can be set ARCHIVED");
            } else {
                version.modificationDate = System.currentTimeMillis();
                version.status = ModelVersion.Status.ARCHIVED;
                version.persist();
            }
        }
        return version;
    }

    @Override
    @Transactional
    public void deleteVersion(String id) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Deleting version with id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", connectedIdentifier, id).singleResultOptional();
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if ( !version.child.isEmpty() ) {
            throw new ConsentManagerException("Unable to delete version that is not last one");
        }
        if ( !version.status.equals(ModelVersion.Status.DRAFT) ) {
            throw new ConsentManagerException("Unable to delete version that is not DRAFT");
        }
        version.delete();
    }

    /* CONSENT MANAGEMENT */

    @Override
    public String buildToken(ConsentContext ctx) {
        //TODO Handle cases where super user is generating a token for another owner
        // For now, authenticated user is enforced as owner of the token :
        LOGGER.log(Level.INFO, "Building generate form token for context: " + ctx);
        ctx.setOwner(authentication.getConnectedIdentifier());
        return tokenService.generateToken(ctx);
    }

    @Override
    public ConsentForm generateForm(String token) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException {
        //TODO :
        // 1. Load existing records for elements of this context (applying models invalidation strategy)
        //    Adapt ConsentForm to include existing values for each elements
        // 2. According to the ConsentContext requisite adopt the correct behaviour for display or not the form or parts of the form
        // 3. If form has to be displayed, load all models to populate
        // 4. Generate a new submission token and populate the form
        LOGGER.log(Level.INFO, "Generating consent form");
        try {
            ConsentContext ctx = (ConsentContext) tokenService.readToken(token);

            List<Record> previousConsents = listRecordsFromContext(ctx);

            ConsentForm form = new ConsentForm();
            form.setLocale(ctx.getLocale());
            form.setOrientation(ctx.getOrientation());

            ModelVersion header = this.systemFindActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
            form.setHeader(header);
            ctx.setHeader(header.getIdentifier().serialize());

            List<String> elementsIdentifiers = new ArrayList<>();
            for (String key : ctx.getElements()) {
                ModelVersion element = this.systemFindActiveVersionByKey(ctx.getOwner(), key);
                previousConsents.stream().filter(record -> record.body.equals(element.serial)).findFirst()
                        .ifPresent(record -> form.addPreviousValue(element.serial, record.value));
                form.addElement(element);
                elementsIdentifiers.add(element.getIdentifier().serialize());
            }
            ctx.setElements(elementsIdentifiers);

            ModelVersion footer = this.systemFindActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
            form.setFooter(footer);
            ctx.setFooter(footer.getIdentifier().serialize());

            form.setToken(tokenService.generateToken(ctx));
            return form;
        } catch ( TokenServiceException e ) {
            throw new ConsentServiceException("Unable to generate consent form", e);
        }
    }

    @Override
    @Transactional
    public Receipt submitConsent(String token, Map<String, String> values) throws InvalidConsentException, TokenExpiredException, InvalidTokenException, ConsentServiceException {
        LOGGER.log(Level.INFO, "Submitting consent");
        try {
            ConsentContext ctx = (ConsentContext) tokenService.readToken(token);

            String transaction = java.util.UUID.randomUUID().toString();
            Instant now = Instant.now();
            this.checkValuesCoherency(ctx, values);

            ConsentElementIdentifier headId = ConsentElementIdentifier.deserialize(ctx.getHeader());
            ConsentElementIdentifier footId = ConsentElementIdentifier.deserialize(ctx.getFooter());
            List<Record> records = new ArrayList<>();
            for (Map.Entry<String, String> value : values.entrySet()) {
                try {
                    ConsentElementIdentifier bodyId = ConsentElementIdentifier.deserialize(value.getKey());
                    Record record = new Record();
                    record.transaction = transaction;
                    record.subject = ctx.getSubject();
                    record.owner = ctx.getOwner();
                    record.head = headId.getSerial();
                    record.type = bodyId.getType();
                    record.body = bodyId.getSerial();
                    record.foot = footId.getSerial();
                    record.serial = headId.getSerial() + "." + bodyId.getSerial() + "." + footId.getSerial();
                    record.value = value.getValue();
                    record.creationTimestamp = now.toEpochMilli();
                    record.expirationTimestamp = now.plus(Period.ofWeeks(52)).toEpochMilli();
                    record.status = Record.Status.COMMITTED;
                    record.persist();
                    records.add(record);
                } catch (IllegalIdentifierException e) {
                    //
                }
            }
            LOGGER.log(Level.INFO, "records: " + records);

            if (!ctx.getReceiptDeliveryType().equals(ConsentContext.ReceiptDeliveryType.NONE)) {
                Header header = (Header) systemFindModelVersionForSerial(headId.getSerial()).getData(ctx.getLocale());
                Footer footer = (Footer) systemFindModelVersionForSerial(footId.getSerial()).getData(ctx.getLocale());
                Map<Treatment, Record> trecords = new HashMap<>();
                records.stream().filter(r -> r.type.equals(Treatment.TYPE)).forEach(r -> {
                    try {
                        Treatment t = (Treatment) systemFindModelVersionForSerial(r.body).getData(ctx.getLocale());
                        trecords.put(t, r);
                    } catch (EntityNotFoundException | ModelDataSerializationException e) {
                        //
                    }
                });
                Receipt receipt = Receipt.build(transaction, processor, now.toEpochMilli(), ctx, header, footer, trecords);
                LOGGER.log(Level.INFO, "Receipt XML: " + receipt.toXml());
                store.put(receipt.getTransaction(), receipt.toXmlBytes());
                return receipt;
            } else {
                return null;
            }
        } catch (TokenServiceException | EntityNotFoundException | ModelDataSerializationException | JAXBException | ReceiptAlreadyExistsException | ReceiptStoreException | IllegalIdentifierException e) {
            //TODO rollback transaction
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    @Override
    public List<Record> listRecordsFromContext(ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Listing records");
        List<Record> records = new ArrayList<>();

        try {
            ModelVersion headerVersion = systemFindActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
            ModelVersion footerVersion = systemFindActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
            for (String elementKey : ctx.getElements()) {
                ModelVersion elementVersion = systemFindActiveVersionByKey(ctx.getOwner(), elementKey);
                Record.find("subject = ?1 and head = ?2 and body = ?3 and foot = ?4", ctx.getSubject(), headerVersion.serial, elementVersion.serial, footerVersion.serial ).stream().forEach(r -> records.add((Record) r));
            }
        } catch (EntityNotFoundException e) {
            LOGGER.log(Level.WARNING, "Entity not found exception: " + e.getMessage());
        }

        LOGGER.log(Level.INFO, "Found " + records.size() + " record(s)");
        return records;
    }

    @Override
    public CollectionPage<Record> listRecords(RecordFilter filter) {
        LOGGER.log(Level.INFO, "Listing records");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        PanacheQuery<Record> query = Record.find(
                "owner = ?1 and subject like ?2 and status = ?3",
                connectedIdentifier,
                "%" + filter.getQuery() + "%",
                Record.Status.COMMITTED);
        CollectionPage<Record> result = new CollectionPage<>();
        result.setValues(query.page(Page.of(filter.getPage(), filter.getSize())).list());
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(query.count());
        return result;
    }

    /* INTERNAL */

    private ModelVersion systemFindActiveVersionByKey(String owner, String key) throws EntityNotFoundException {
        Optional<ModelVersion> optional = ModelVersion.find("owner = ?1 and entry.key = ?2 and status = ?3", owner, key, ModelVersion.Status.ACTIVE).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an active version for entry with key: " + key + " and owner: " + owner));
    }

    private ModelVersion systemFindActiveVersionByEntryId(String owner, String entryId) throws EntityNotFoundException {
        Optional<ModelVersion> optional = ModelVersion.find("owner = ?1 and entry.id = ?2 and status = ?3", owner, entryId, ModelVersion.Status.ACTIVE).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an active version for entry with id: " + entryId + " and owner: " + owner));
    }

    private ModelVersion systemFindModelVersionForSerial(String serial) throws EntityNotFoundException {
        Optional<ModelVersion> optional = ModelVersion.find("serial = ?1", serial).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for serial: " + serial));
    }

    private void checkValuesCoherency(ConsentContext ctx, Map<String, String> values) throws InvalidConsentException {
        if ( ctx.getHeader() == null && values.containsKey("header") ) {
            throw new InvalidConsentException("submitted header incoherency, expected: null got: " + values.get("header"));
        }
        if ( ctx.getHeader() != null && !ctx.getHeader().isEmpty() && (!values.containsKey("header") || !values.get("header").equals(ctx.getHeader())) ) {
            throw new InvalidConsentException("submitted header incoherency, expected: " + ctx.getHeader() + " got: " + values.get("header"));
        }
        if ( ctx.getFooter() == null && values.containsKey("footer") ) {
            throw new InvalidConsentException("submitted footer incoherency, expected: null got: " + values.get("footer"));
        }
        if ( ctx.getFooter() != null && !ctx.getFooter().isEmpty() && (!values.containsKey("footer") || !values.get("footer").equals(ctx.getFooter())) ) {
            throw new InvalidConsentException("submitted footer incoherency, expected: " + ctx.getFooter() + " got: " + values.get("footer"));
        }
        Map<String, String> submittedElementValues = values.entrySet().stream().filter(e -> e.getKey().startsWith("element")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if ( !new HashSet<>(ctx.getElements()).equals(submittedElementValues.keySet()) ) {
            throw new InvalidConsentException("submitted elements incoherency");
        }
    }

}
