package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.handler.context.ConsentContextHandler;
import com.fairandsmart.consent.api.resource.ConsentsResource;
import com.fairandsmart.consent.common.util.PageUtil;
import com.fairandsmart.consent.manager.filter.MixedRecordsFilter;
import com.fairandsmart.consent.manager.model.*;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.util.SortUtil;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.filter.UserRecordFilter;
import com.fairandsmart.consent.manager.store.ReceiptAlreadyExistsException;
import com.fairandsmart.consent.manager.store.ReceiptStore;
import com.fairandsmart.consent.manager.store.ReceiptStoreException;
import com.fairandsmart.consent.notification.NotificationService;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
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
    TokenService token;

    @Inject
    ReceiptStore store;

    @Inject
    Instance<ConsentContextHandler> contextHandlers;

    @Inject
    NotificationService notification;

    @ConfigProperty(name = "consent.processor")
    String processor;

    @ConfigProperty(name = "consent.public.url")
    String publicUrl;

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
        return PageUtil.paginateQuery(query, filter);
    }

    @Override
    @Transactional
    public ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "Creating new entry");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        if (ModelEntry.isKeyAlreadyExistsForOwner(connectedIdentifier, key)) {
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
        if (!entry.owner.equals(connectedIdentifier)) {
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
        if (versions.isEmpty() || versions.stream().allMatch(v -> v.status.equals(ModelVersion.Status.DRAFT))) {
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
        if (!entry.type.equals(data.getType())) {
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
                newversion.parent = latest.id;
                newversion.defaultLocale = latest.defaultLocale;
                newversion.availableLocales = latest.availableLocales;
                newversion.content.putAll(latest.content);
                newversion.counterparts = latest.counterparts;
                newversion.type = ModelVersion.Type.MINOR;
                newversion.addCounterpart(latest.serial);

                latest.child = newversion.id;
                latest.persist();
                latest = newversion;
            }
            latest.addAvailableLocale(locale);
            latest.content.put(locale, new ModelContent().withDataObject(data).withAuthor(connectedIdentifier));
            latest.modificationDate = now;
            latest.persist();
            return latest;
        } catch (SerialGeneratorException ex) {
            throw new ConsentManagerException("unable to generate serial number for new version", ex);
        } catch (ModelDataSerializationException ex) {
            throw new ConsentManagerException("unable to serialise data", ex);
        }
    }

    @Override
    public ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding active version for entry with key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        return ModelVersion.SystemHelper.findActiveVersionByKey(connectedIdentifier, key);
    }

    @Override
    public ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding active version for entry with id: " + entryId);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        return ModelVersion.SystemHelper.findActiveVersionByEntryId(connectedIdentifier, entryId);
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
        if (!version.owner.equals(connectedIdentifier)) {
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
        if (!versions.isEmpty()) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    public List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Listing versions for entry with id: " + entryId);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.id = ?2", connectedIdentifier, entryId).list();
        if (!versions.isEmpty()) {
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
        if (!version.entry.type.equals(data.getType())) {
            throw new ConsentManagerException("Entry data type mismatch, need type: " + version.entry.type);
        }
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to update content for version that is not last one");
        }
        try {
            version.addAvailableLocale(locale);
            version.content.put(locale, new ModelContent().withDataObject(data).withAuthor(connectedIdentifier));
            version.modificationDate = System.currentTimeMillis();
            version.persist();
            return version;
        } catch (ModelDataSerializationException ex) {
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
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to update type for version that is not last one");
        }
        if (!version.status.equals(ModelVersion.Status.DRAFT)) {
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
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to update status of a version that is not the latest");
        }
        if (status.equals(ModelVersion.Status.DRAFT)) {
            throw new InvalidStatusException("Unable to update a version to DRAFT status");
        }
        if (status.equals(ModelVersion.Status.ACTIVE)) {
            if (!version.status.equals(ModelVersion.Status.DRAFT)) {
                throw new InvalidStatusException("Only DRAFT version can be set ACTIVE");
            } else {
                if (!version.parent.isEmpty()) {
                    ModelVersion parent = ModelVersion.findById(version.parent);
                    parent.modificationDate = System.currentTimeMillis();
                    parent.status = ModelVersion.Status.ARCHIVED;
                    parent.persist();
                }
                version.status = ModelVersion.Status.ACTIVE;
                version.modificationDate = System.currentTimeMillis();
                if (version.type.equals(ModelVersion.Type.MAJOR)) {
                    version.counterparts = "";
                }
                version.persist();
            }
        }
        if (status.equals(ModelVersion.Status.ARCHIVED)) {
            if (!version.status.equals(ModelVersion.Status.ACTIVE)) {
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
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to delete version that is not last one");
        }
        if (!version.status.equals(ModelVersion.Status.DRAFT)) {
            throw new ConsentManagerException("Unable to delete version that is not DRAFT");
        }
        version.delete();
    }

    /* CONSENT MANAGEMENT */

    @Override
    public String buildToken(ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Building generate form token for context: " + ctx);
        ctx.setOwner(authentication.getConnectedIdentifier());
        return token.generateToken(ctx);
    }

    @Override
    public ConsentForm generateForm(String token, String subject) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException {
        //TODO :
        // 1. Load existing records for elements of this context (applying models invalidation strategy)
        //    Adapt ConsentForm to include existing values for each elements
        // 2. According to the ConsentContext requisite adopt the correct behaviour for display or not the form or parts of the form
        // 3. If form has to be displayed, load all models to populate
        // 4. Generate a new submission token and populate the form

        //TODO Handle case of an optout token (models are already ids and not keys...)
        LOGGER.log(Level.INFO, "Generating consent form");
        try {
            ConsentContext ctx = (ConsentContext) this.token.readToken(token);
            if (StringUtils.isEmpty(ctx.getSubject())) {
                if (!StringUtils.isEmpty(subject)) {
                    ctx.setSubject(subject);
                } else if (!ctx.isPreview()) {
                    throw new ConsentServiceException("Subject is empty");
                }
            }

            List<Record> previousConsents = new ArrayList<>();
            if (!ctx.isConditions() && !ctx.isPreview()) {
                previousConsents = findRecordsForContext(ctx);
            }

            ConsentForm form = new ConsentForm();
            form.setLocale(ctx.getLocale());
            form.setOrientation(ctx.getOrientation());
            form.setPreview(ctx.isPreview());
            form.setConditions(ctx.isConditions());

            if (!StringUtils.isEmpty(ctx.getHeader())) {
                ModelVersion header = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
                form.setHeader(header);
                ctx.setHeader(header.getIdentifier().serialize());
            }

            List<String> elementsIdentifiers = new ArrayList<>();
            for (String key : ctx.getElements()) {
                ModelVersion element = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), key);
                previousConsents.stream().filter(r -> r.bodyKey.equals(key)).findFirst().ifPresent(r -> form.addPreviousValue(element.serial, r.value));
                if (ctx.getFormType().equals(ConsentContext.FormType.FULL) || !form.getPreviousValues().containsKey(element.serial)) {
                    form.addElement(element);
                    elementsIdentifiers.add(element.getIdentifier().serialize());
                }
            }
            ctx.setElements(elementsIdentifiers);

            if (!StringUtils.isEmpty(ctx.getFooter())) {
                ModelVersion footer = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
                form.setFooter(footer);
                ctx.setFooter(footer.getIdentifier().serialize());
            }

            if (!StringUtils.isEmpty(ctx.getTheme())) {
                ModelVersion theme = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getTheme());
                form.setTheme(theme);
                ctx.setTheme(theme.getIdentifier().serialize());
            }

            if (!StringUtils.isEmpty(ctx.getOptoutModel())) {
                ModelVersion optout = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getOptoutModel());
                ctx.setOptoutModel(optout.getIdentifier().serialize());
            }

            form.setToken(this.token.generateToken(ctx));
            return form;
        } catch (TokenServiceException | ConsentServiceException e) {
            throw new ConsentServiceException("Unable to generate consent form", e);
        }
    }

    @Override
    public ConsentForm generateLipsumForm(ConsentForm.Orientation orientation, String locale) throws ModelDataSerializationException {
        ConsentForm form = new ConsentForm();
        form.setLocale(locale);
        form.setOrientation(orientation);
        form.setPreview(true);
        form.setConditions(false);
        form.setToken("PREVIEW");

        try {
            ObjectMapper mapper = new ObjectMapper();
            CollectionType dataTypeRef =
                    TypeFactory.defaultInstance().constructCollectionType(List.class, ModelData.class);
            String jsonFileName = "preview/lipsum.json";
            URL jsonFile = getClass().getClassLoader().getSystemResource(jsonFileName);
            if (jsonFile == null) jsonFile = getClass().getClassLoader().getResource(jsonFileName);
            List<ModelData> data = mapper.readValue(jsonFile, dataTypeRef);

            form.setHeader(generateVersionForPreview(locale, data.get(0)));
            form.addElement(generateVersionForPreview(locale, data.get(1)));
            form.addElement(generateVersionForPreview(locale, data.get(2)));
            form.setFooter(generateVersionForPreview(locale, data.get(3)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return form;
    }

    @Override
    @Transactional
    public Receipt submitConsent(String token, MultivaluedMap<String, String> values) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, InvalidConsentException {
        LOGGER.log(Level.INFO, "Submitting consent");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        try {
            ConsentContext ctx = (ConsentContext) this.token.readToken(token);
            if (StringUtils.isEmpty(ctx.getSubject())) {
                throw new ConsentServiceException("Subject is empty");
            }

            Map<String, String> valuesMap = new HashMap<>();
            for (MultivaluedMap.Entry<String, List<String>> value : values.entrySet()) {
                valuesMap.put(value.getKey(), value.getValue().get(0));
            }
            //TODO Check that identifiers refers to the latest elements versions to avoid committing a consent on a staled version of an entry
            // (aka form is generated before a MAJOR RELEASE and submit after)
            // Maybe use a global referential hash that would be injected in token and which could be stored as a whole database integrity check
            // Any change in a entry would modify this hash and avoid checking each element but only a in memory or in database single value
            Receipt receipt = this.saveConsent(ctx, valuesMap, "");

            if (!StringUtils.isEmpty(ctx.getOptoutRecipient())) {
                Event<ConsentOptOut> event = new Event().withType(Event.CONSENT_OPTOUT).withAuthor(connectedIdentifier);
                if (!StringUtils.isEmpty(ctx.getOptoutModel())) {
                    try {
                        ConsentOptOut optout = new ConsentOptOut();
                        optout.setLocale(ctx.getLocale());
                        optout.setRecipient(ctx.getOptoutRecipient());
                        ModelVersion optoutModel = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getOptoutModel()).getSerial());
                        optout.setModel(optoutModel);
                        ctx.setOptoutModel(optoutModel.entry.key);
                        if (!StringUtils.isEmpty(ctx.getTheme())) {
                            ModelVersion theme = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getTheme()).getSerial());
                            optout.setTheme(theme);
                            ctx.setTheme(theme.entry.key);
                        }
                        if (!StringUtils.isEmpty(ctx.getHeader())) {
                            ctx.setHeader(ConsentElementIdentifier.deserialize(ctx.getHeader()).getKey());
                        }
                        if (!StringUtils.isEmpty(ctx.getFooter())) {
                            ctx.setFooter(ConsentElementIdentifier.deserialize(ctx.getFooter()).getKey());
                        }
                        List<String> celements = new ArrayList<>();
                        for ( String element : ctx.getElements() ) {
                            celements.add(ConsentElementIdentifier.deserialize(element).getKey());
                        }
                        ctx.setElements(celements);
                        ctx.setOptoutRecipient("");
                        ctx.setOptoutModel("");
                        optout.setToken(this.token.generateToken(ctx));
                        URI optoutUri = UriBuilder.fromUri(publicUrl).path(ConsentsResource.class).queryParam("t", optout.getToken()).build();
                        optout.setUrl(optoutUri.toString());
                        notification.notify(event.withData(optout));
                    } catch (EntityNotFoundException | IllegalIdentifierException e ) {
                        LOGGER.log(Level.SEVERE, "Unable to load optout model", e);
                    }
                } else {
                    //TODO use a default model
                    LOGGER.log(Level.SEVERE, "No optout model set but an optout recipient, Default MODEL NOT IMPLEMENTED YET");
                }
            }
            return receipt;
        } catch (TokenServiceException | ConsentServiceException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    @Override
    public List<Record> findRecordsForContext(ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Listing records");
        List<Record> records = new ArrayList<>();

        try {
            //TODO Maybe add also a condition on the status (COMMITTED)
            for (ConsentContextHandler handler : contextHandlers) {
                if (handler.canHandle(ctx)) {
                    records.addAll(handler.findRecords(ctx));
                }
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
        PanacheQuery<Record> query;
        Sort sort = SortUtil.fromFilter(filter);
        if (sort != null) {
            query = Record.find("owner = ?1 and subject like ?2 and status = ?3", sort, connectedIdentifier, "%" + filter.getQuery() + "%", Record.Status.COMMITTED);
        } else {
            query = Record.find("owner = ?1 and subject like ?2 and status = ?3", connectedIdentifier, "%" + filter.getQuery() + "%", Record.Status.COMMITTED);
        }
        return PageUtil.paginateQuery(query, filter);
    }

    @Override
    public CollectionPage<UserRecord> listUserRecords(UserRecordFilter filter) {
        List<ModelEntry> entries = ModelEntry.find(
                "owner = ?1 and type in ?2",
                authentication.getConnectedIdentifier(),
                Arrays.asList(Treatment.TYPE, Conditions.TYPE)).list();

        List<UserRecord> userRecords = new ArrayList<>();
        entries.forEach(entry -> userRecords.add(findUserRecord(entry, filter)));
        userRecords.sort((r1, r2) -> r1.compare(r2, filter));

        return PageUtil.paginateList(userRecords, filter);
    }

    @Override
    public CollectionPage<UserRecord> listRecordsForUsers(MixedRecordsFilter filter) {
        String connectedIdentifier = authentication.getConnectedIdentifier();
        List<ModelEntry> entries = new ArrayList<>();

        if (filter.getTreatments().size() > 0) {
            entries.addAll(ModelEntry.find(
                    "owner = ?1 and key in ?2 and type = ?3",
                    connectedIdentifier,
                    filter.getTreatments(),
                    Treatment.TYPE).list());
        }
        if (filter.getConditions().size() > 0) {
            entries.addAll(ModelEntry.find(
                    "owner = ?1 and key in ?2 and type = ?3",
                    connectedIdentifier,
                    filter.getConditions(),
                    Conditions.TYPE).list());
        }

        List<UserRecord> records = new ArrayList<>();
        entries.forEach(entry -> filter.getUsers().forEach(user -> records.add(findRecordForUser(entry, user))));
        records.sort((r1, r2) -> r1.compare(r2, filter));

        return PageUtil.paginateList(records, filter);
    }

    @Override
    @Transactional
    public Receipt createOperatorRecords(String token, Map<String, String> values, String comment) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, InvalidConsentException {
        LOGGER.log(Level.INFO, "Creating record for operator");
        try {
            ConsentContext ctx = (ConsentContext) this.token.readToken(token);

            ModelVersion headerVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
            ctx.setHeader(headerVersion.getIdentifier().serialize());
            ModelVersion footerVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
            ctx.setFooter(footerVersion.getIdentifier().serialize());
            List<String> newElements = new ArrayList<>();
            for (String element : ctx.getElements()) {
                ModelVersion elementVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), element);
                newElements.add(elementVersion.getIdentifier().serialize());
            }
            ctx.setElements(newElements);

            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("header", headerVersion.getIdentifier().serialize());
            valuesMap.put("footer", footerVersion.getIdentifier().serialize());
            for (Map.Entry<String, String> value : values.entrySet()) {
                ModelVersion bodyVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), value.getKey());
                valuesMap.put(bodyVersion.getIdentifier().serialize(), value.getValue());
            }

            return this.saveConsent(ctx, valuesMap, comment);
        } catch (TokenServiceException | ConsentServiceException | EntityNotFoundException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    /* INTERNAL */

    private UserRecord findUserRecord(ModelEntry entry, UserRecordFilter filter) {
        UserRecord record = findRecordForUser(entry, filter.getUser());
        boolean isFilterCompliant = true;
        if (!StringUtils.isEmpty(record.getValue())) {
            if (!StringUtils.isEmpty(filter.getCollectionMethod()) && !filter.getCollectionMethod().equals(record.getCollectionMethod())) {
                isFilterCompliant = false;
            }
            if (!StringUtils.isEmpty(filter.getValue()) && !filter.getValue().equals(record.getValue())) {
                isFilterCompliant = false;
            }
            if (filter.getDateAfter() > 0 && filter.getDateAfter() > record.getCreationTimestamp()) {
                isFilterCompliant = false;
            }
            if (filter.getDateBefore() > 0 && filter.getDateBefore() < record.getCreationTimestamp()) {
                isFilterCompliant = false;
            }
        }
        return isFilterCompliant ? record : UserRecord.fromEntryAndSubject(entry, filter.getUser());
    }

    private UserRecord findRecordForUser(ModelEntry entry, String user) {
        Optional<Record> optional = Record.find(
                "owner = ?1 and type = ?2 and bodyKey = ?3 and subject = ?4",
                Sort.by("creationTimestamp", Sort.Direction.Descending),
                entry.owner,
                entry.type,
                entry.key,
                user)
                .firstResultOptional();

        if (optional.isPresent()) {
            return UserRecord.fromRecord(optional.get());
        } else {
            return UserRecord.fromEntryAndSubject(entry, user);
        }
    }

    private void checkValuesCoherency(ConsentContext ctx, Map<String, String> values) throws InvalidConsentException {
        if (ctx.getHeader() == null && values.containsKey("header")) {
            throw new InvalidConsentException("submitted header incoherency, expected: null got: " + values.get("header"));
        }
        if (!StringUtils.isEmpty(ctx.getHeader()) && (!values.containsKey("header") || !values.get("header").equals(ctx.getHeader()))) {
            throw new InvalidConsentException("submitted header incoherency, expected: " + ctx.getHeader() + " got: " + values.get("header"));
        }
        if (ctx.getFooter() == null && values.containsKey("footer")) {
            throw new InvalidConsentException("submitted footer incoherency, expected: null got: " + values.get("footer"));
        }
        if (!StringUtils.isEmpty(ctx.getFooter()) && (!values.containsKey("footer") || !values.get("footer").equals(ctx.getFooter()))) {
            throw new InvalidConsentException("submitted footer incoherency, expected: " + ctx.getFooter() + " got: " + values.get("footer"));
        }
        Map<String, String> submittedElementValues = values.entrySet().stream()
                .filter(e -> e.getKey().startsWith("element"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (!new HashSet<>(ctx.getElements()).equals(submittedElementValues.keySet())) {
            throw new InvalidConsentException("submitted elements incoherency");
        }
    }

    private Receipt saveConsent(ConsentContext ctx, Map<String, String> values, String comment) throws ConsentServiceException, InvalidConsentException {
        try {
            this.checkValuesCoherency(ctx, values);
            String transaction = java.util.UUID.randomUUID().toString();
            Instant now = Instant.now();

            ConsentElementIdentifier headId = null;
            if (!StringUtils.isEmpty(ctx.getHeader())) {
                headId = ConsentElementIdentifier.deserialize(ctx.getHeader());
            }
            ConsentElementIdentifier footId = null;
            if (!StringUtils.isEmpty(ctx.getFooter())) {
                footId = ConsentElementIdentifier.deserialize(ctx.getFooter());
            }
            List<Record> records = new ArrayList<>();
            for (Map.Entry<String, String> value : values.entrySet()) {
                try {
                    ConsentElementIdentifier bodyId = ConsentElementIdentifier.deserialize(value.getKey());
                    Record record = new Record();
                    record.transaction = transaction;
                    record.subject = ctx.getSubject();
                    record.owner = ctx.getOwner();
                    record.type = bodyId.getType();
                    record.headSerial = headId != null ? headId.getSerial() : "";
                    record.bodySerial = bodyId.getSerial();
                    record.footSerial = footId != null ? footId.getSerial() : "";
                    record.headKey = headId != null ? headId.getKey() : "";
                    record.bodyKey = bodyId.getKey();
                    record.footKey = footId != null ? footId.getKey() : "";
                    record.serial = record.headSerial + "." + record.bodySerial + "." + record.footSerial;
                    record.value = value.getValue();
                    record.creationTimestamp = now.toEpochMilli();
                    record.expirationTimestamp = ctx.isConditions() ? 0 : now.plusMillis(ctx.getValidityInMillis()).toEpochMilli();
                    record.status = Record.Status.COMMITTED;
                    record.collectionMethod = ctx.getCollectionMethod();
                    record.author = !StringUtils.isEmpty(ctx.getAuthor()) ? ctx.getAuthor() : ctx.getOwner();
                    record.comment = comment;
                    record.persist();
                    records.add(record);
                } catch (IllegalIdentifierException e) {
                    //
                }
            }
            LOGGER.log(Level.INFO, "records: " + records);

            Receipt receipt;
            if (ctx.getReceiptDeliveryType().equals(ConsentContext.ReceiptDeliveryType.NONE)) {
                receipt = new Receipt();
                receipt.setLocale(ctx.getLocale());
            } else {
                Header header = null;
                if (headId != null) {
                    header = (Header) ModelVersion.SystemHelper.findModelVersionForSerial(headId.getSerial()).getData(ctx.getLocale());
                }
                Footer footer = null;
                if (footId != null) {
                    footer = (Footer) ModelVersion.SystemHelper.findModelVersionForSerial(footId.getSerial()).getData(ctx.getLocale());
                }
                Map<Treatment, Record> trecords = new HashMap<>();
                records.stream().filter(r -> r.type.equals(Treatment.TYPE)).forEach(r -> {
                    try {
                        Treatment t = (Treatment) ModelVersion.SystemHelper.findModelVersionForSerial(r.bodySerial).getData(ctx.getLocale());
                        trecords.put(t, r);
                    } catch (EntityNotFoundException | ModelDataSerializationException e) {
                        //
                    }
                });
                receipt = Receipt.build(transaction, processor, now.toEpochMilli(), ctx, header, footer, trecords);
                LOGGER.log(Level.INFO, "Receipt XML: " + receipt.toXml());
                store.put(receipt.getTransaction(), receipt.toXmlBytes());
            }
            return receipt;
        } catch (EntityNotFoundException | ModelDataSerializationException | JAXBException | ReceiptAlreadyExistsException | ReceiptStoreException | IllegalIdentifierException | DatatypeConfigurationException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    private ModelVersion generateVersionForPreview(String locale, ModelData data) throws ModelDataSerializationException {
        ModelContent previewContent = new ModelContent();
        previewContent.setDataObject(data);

        ModelEntry previewEntry = new ModelEntry();
        previewEntry.key = "PREVIEW";
        previewEntry.type = data.getType();

        ModelVersion previewVersion = new ModelVersion();
        previewVersion.type = ModelVersion.Type.MAJOR;
        previewVersion.serial = "PREVIEW";
        previewVersion.defaultLocale = locale;
        previewVersion.availableLocales = locale;
        previewVersion.entry = previewEntry;
        previewVersion.content.put(locale, previewContent);
        return previewVersion;
    }
}
