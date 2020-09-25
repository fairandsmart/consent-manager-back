package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.manager.cache.PreviewCache;
import com.fairandsmart.consent.api.resource.ConsentsResource;
import com.fairandsmart.consent.common.util.PageUtil;
import com.fairandsmart.consent.manager.model.*;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.util.SortUtil;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.rule.*;
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
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    Instance<ReceiptStore> stores;

    @Inject
    NotificationService notification;

    @Inject
    MainConfig config;

    /* Keys are ModelEntries ids since only one version per entry can be previewed. It helps clearing the cache when an entry is deleted. */
    @Inject
    PreviewCache previewCache;

    @Inject
    RecordStatusFitlerChain statusFilterChain;

    /* MODELS MANAGEMENT */

    @Override
    public CollectionPage<ModelEntry> listEntries(ModelFilter filter) {
        LOGGER.log(Level.INFO, "Listing models entries");
        PanacheQuery<ModelEntry> query;
        filter.setOwner(config.owner());
        Sort sort = SortUtil.fromFilter(filter);
        if (sort != null) {
            query = ModelEntry.find(filter.getQueryString(), sort, filter.getQueryParams());
        } else {
            query = ModelEntry.find(filter.getQueryString(), filter.getQueryParams());
        }
        return PageUtil.paginateQuery(query, filter);
    }

    @Override
    @Transactional
    public ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Creating new entry");
        authentication.ensureConnectedIdentifierIsAdmin();
        if (ModelEntry.isKeyAlreadyExistsForOwner(config.owner(), key)) {
            throw new EntityAlreadyExistsException("A model entry already exists with key: " + key);
        }
        ModelEntry entry = new ModelEntry();
        entry.type = type;
        entry.key = key;
        entry.name = name;
        entry.description = description;
        entry.branches = DEFAULT_BRANCHE;
        entry.author = authentication.getConnectedIdentifier();
        entry.owner = config.owner();
        entry.persist();
        return entry;
    }

    @Override
    public ModelEntry getEntry(String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Getting entry for id: " + id);
        authentication.ensureConnectedIdentifierIsOperator();
        Optional<ModelEntry> optional = ModelEntry.findByIdOptional(id);
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + id));
        if (!entry.owner.equals(config.owner())) {
            throw new AccessDeniedException("access denied to version with id: " + id);
        }
        return entry;
    }

    @Override
    public ModelEntry findEntryForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding entry for key: " + key);
        Optional<ModelEntry> optional = ModelEntry.find("owner = ?1 and key = ?2", config.owner(), key).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for key: " + key));
    }

    @Override
    @Transactional
    public ModelEntry updateEntry(String id, String name, String description) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Updating entry for id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelEntry> optional = ModelEntry.find("id = ?1 and owner = ?2", id, config.owner()).singleResultOptional();
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
        authentication.ensureConnectedIdentifierIsAdmin();
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.id = ?2", config.owner(), id).list();
        if (versions.isEmpty() || versions.stream().allMatch(v -> v.status.equals(ModelVersion.Status.DRAFT))) {
            ModelEntry.deleteById(id);
            previewCache.remove(id);
        } else {
            //TODO Maybe allow this but ensure that all corresponding records are going to be deleted... and that receipt may be corrupted.
            throw new ConsentManagerException("unable to delete entry that have not only DRAFT versions");
        }
    }

    @Override
    @Transactional
    public ModelVersion createVersion(String entryId, String defaultLocale, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Creating new version for entry with id: " + entryId);
        authentication.ensureConnectedIdentifierIsAdmin();
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelEntry> optional = ModelEntry.find("id = ?1 and owner = ?2", entryId, config.owner()).singleResultOptional();
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + entryId));
        if (data.values().stream().anyMatch(d -> !d.getType().equals(entry.type))) {
            throw new ConsentManagerException("One content data type does not belongs to entry type: " + entry.type);
        }
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and entry.id = ?2 and child = ?3", config.owner(), entryId, "").singleResultOptional();
        ModelVersion latest = voptional.orElse(null);
        try {
            long now = System.currentTimeMillis();
            if (latest == null) {
                LOGGER.log(Level.INFO, "No existing version found, creating first one");
                latest = new ModelVersion();
                latest.entry = entry;
                latest.author = connectedIdentifier;
                latest.owner = config.owner();
                latest.branches = DEFAULT_BRANCHE;
                latest.creationDate = now;
                latest.status = ModelVersion.Status.DRAFT;
                latest.serial = generator.next(ModelVersion.class.getName());
                latest.type = ModelVersion.Type.MINOR;
            } else if (latest.status.equals(ModelVersion.Status.DRAFT)) {
                throw new ConsentManagerException("A draft version already exists, unable to create new one");
            } else {
                LOGGER.log(Level.INFO, "Latest version found, creating new one");
                ModelVersion newversion = new ModelVersion();
                newversion.entry = latest.entry;
                newversion.author = connectedIdentifier;
                newversion.owner = config.owner();
                newversion.branches = DEFAULT_BRANCHE;
                newversion.creationDate = now;
                newversion.status = ModelVersion.Status.DRAFT;
                newversion.serial = generator.next(ModelVersion.class.getName());
                newversion.parent = latest.id;
                newversion.counterparts = latest.counterparts;
                newversion.type = ModelVersion.Type.MINOR;
                newversion.addCounterpart(latest.serial);
                newversion.persist();

                latest.child = newversion.id;
                latest.persist();
                latest = newversion;
            }
            latest.content.clear();
            for (Map.Entry<String, ModelData> e : data.entrySet()) {
                latest.content.put(e.getKey(), new ModelContent().withAuthor(connectedIdentifier).withDataObject(e.getValue()));
            }
            latest.availableLocales = String.join(",", data.keySet());
            if (latest.content.containsKey(defaultLocale)) {
                latest.defaultLocale = defaultLocale;
            } else {
                throw new ConsentManagerException("Default Locale does not exists in content locales");
            }
            latest.modificationDate = now;
            latest.persist();
            if (previewCache.containsKey(latest.entry.id)) {
                previewCache.put(latest.entry.id, latest);
            }
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
        return ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), key);
    }

    @Override
    public ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding active version for entry with id: " + entryId);
        return ModelVersion.SystemHelper.findActiveVersionByEntryId(config.owner(), entryId);
    }

    @Override
    public ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding latest version for entry with key: " + key);
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and entry.key = ?2 and child = ?3", config.owner(), key, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with key: " + key));
    }

    @Override
    public ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding latest version for entry with id: " + entryId);
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and entry.id = ?2 and child = ?3", config.owner(), entryId, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with id: " + entryId));
    }

    @Override
    public ModelVersion getVersion(String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Finding version for id: " + id);
        Optional<ModelVersion> optional = ModelVersion.findByIdOptional(id);
        ModelVersion version = optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for id: " + id));
        if (!version.owner.equals(config.owner())) {
            throw new AccessDeniedException("access denied to version with id: " + id);
        }
        return version;
    }

    @Override
    public ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding version for serial: " + serial);
        Optional<ModelVersion> optional = ModelVersion.find("owner = ?1 and serial = ?2", config.owner(), serial).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for serial: " + serial));
    }

    @Override
    public List<ModelVersion> getVersionHistoryForKey(String key) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Listing versions for entry with key: " + key);
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.key = ?2", config.owner(), key).list();
        if (!versions.isEmpty()) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    public List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Listing versions for entry with id: " + entryId);
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.id = ?2", config.owner(), entryId).list();
        if (!versions.isEmpty()) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    @Transactional
    public ModelVersion updateVersion(String id, String defaultLocale, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating content for version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", config.owner(), id).singleResultOptional();
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if (data.values().stream().anyMatch(d -> !d.getType().equals(version.entry.type))) {
            throw new ConsentManagerException("One content data type does not belongs to entry type: " + version.entry.type);
        }
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to update content for version that is not last one");
        }
        try {
            version.content.clear();
            for (Map.Entry<String, ModelData> entry : data.entrySet()) {
                version.content.put(entry.getKey(), new ModelContent().withAuthor(connectedIdentifier).withDataObject(entry.getValue()));
            }
            version.availableLocales = String.join(",", data.keySet());
            if (version.content.containsKey(defaultLocale)) {
                version.defaultLocale = defaultLocale;
            } else {
                throw new ConsentManagerException("Default Locale does not exists in content locales");
            }
            version.modificationDate = System.currentTimeMillis();
            version.persist();
            if (previewCache.containsKey(version.entry.id)) {
                previewCache.put(version.entry.id, version);
            }
            return version;
        } catch (ModelDataSerializationException ex) {
            throw new ConsentManagerException("unable to serialise data", ex);
        }

    }

    @Override
    @Transactional
    public ModelVersion updateVersionType(String id, ModelVersion.Type type) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating type for version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", config.owner(), id).singleResultOptional();
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
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", config.owner(), id).singleResultOptional();
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
    public PreviewDto previewVersion(String entryId, String versionId, PreviewDto dto) throws AccessDeniedException, EntityNotFoundException, ModelDataSerializationException {
        ModelVersion version = previewCache.lookup(entryId);
        if (version == null) {
            if (versionId != null && !versionId.equals("new")) {
                version = this.getVersion(versionId);
                if (!version.entry.id.equals(entryId)) {
                    throw new EntityNotFoundException("Unable to find a version with id: " + versionId + " in entry with id: " + entryId);
                }
            } else {
                version = new ModelVersion();
                version.entry = this.getEntry(entryId);
            }
        }

        if (dto.getData() != null) {
            version.content.put(dto.getLocale(), new ModelContent().withDataObject(dto.getData()));
            previewCache.put(entryId, version);
        } else {
            dto.setData(version.content.get(dto.getLocale()).getDataObject());
        }
        return dto;
    }

    @Override
    @Transactional
    public void deleteVersion(String id) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Deleting version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelVersion> voptional = ModelVersion.find("owner = ?1 and id = ?2", config.owner(), id).singleResultOptional();
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
    public String buildToken(ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Building generate form token for context: " + ctx);
        if (ctx.getSubject() == null || ctx.getSubject().isEmpty()) {
            ctx.setSubject(authentication.getConnectedIdentifier());
        } else if (!ctx.getSubject().equals(authentication.getConnectedIdentifier()) && !authentication.isConnectedIdentifierAdmin()) {
            throw new AccessDeniedException("Only admin can generate token for other identifier than connected one");
        }
        return token.generateToken(ctx);
    }

    @Override
    public ConsentForm generateForm(String token) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException {
        //TODO Handle case of an optout token (models are already ids and not keys...)
        LOGGER.log(Level.INFO, "Generating consent form");
        try {
            ConsentContext ctx = (ConsentContext) this.token.readToken(token);

            List<Record> previousConsents = new ArrayList<>();
            if (!ctx.isConditions() && !ctx.isPreview()) {
                previousConsents = systemFindRecordsForContext(ctx);
            }

            ConsentForm form = new ConsentForm();
            form.setLocale(ctx.getLocale());
            form.setOrientation(ctx.getOrientation());
            form.setPreview(ctx.isPreview());
            form.setConditions(ctx.isConditions());

            if (!StringUtils.isEmpty(ctx.getHeader())) {
                ModelVersion header = ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), ctx.getHeader());
                form.setHeader(header);
                ctx.setHeader(header.getIdentifier().serialize());
            }

            List<String> elementsIdentifiers = new ArrayList<>();
            for (String key : ctx.getElements()) {
                ModelVersion element = ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), key);
                previousConsents.stream().filter(r -> r.bodyKey.equals(key)).findFirst().ifPresent(r -> form.addPreviousValue(element.serial, r.value));
                if (ctx.getFormType().equals(ConsentContext.FormType.FULL) || !form.getPreviousValues().containsKey(element.serial)) {
                    form.addElement(element);
                    elementsIdentifiers.add(element.getIdentifier().serialize());
                }
            }
            ctx.setElements(elementsIdentifiers);

            if (!StringUtils.isEmpty(ctx.getFooter())) {
                ModelVersion footer = ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), ctx.getFooter());
                form.setFooter(footer);
                ctx.setFooter(footer.getIdentifier().serialize());
            }

            if (!StringUtils.isEmpty(ctx.getTheme())) {
                ModelVersion theme = ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), ctx.getTheme());
                form.setTheme(theme);
                ctx.setTheme(theme.getIdentifier().serialize());
            }

            if (!StringUtils.isEmpty(ctx.getOptoutModel())) {
                ModelVersion optout = ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), ctx.getOptoutModel());
                ctx.setOptoutModel(optout.getIdentifier().serialize());
            }

            form.setToken(this.token.generateToken(ctx));
            return form;
        } catch (TokenServiceException e) {
            throw new ConsentServiceException("Unable to generate consent form", e);
        }
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
                        //TODO avoid converting element identifier to key but modify the getForm method
                        ModelVersion optoutModel = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getOptoutModel()).getSerial(), true);
                        optout.setModel(optoutModel);
                        ctx.setOptoutModel(optoutModel.entry.key);
                        if (!StringUtils.isEmpty(ctx.getTheme())) {
                            ModelVersion theme = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getTheme()).getSerial(), true);
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
                        for (String element : ctx.getElements()) {
                            celements.add(ConsentElementIdentifier.deserialize(element).getKey());
                        }
                        ctx.setElements(celements);
                        ctx.setOptoutRecipient("");
                        ctx.setOptoutModel("");
                        optout.setToken(this.token.generateToken(ctx));
                        URI optoutUri = UriBuilder.fromUri(config.publicUrl()).path(ConsentsResource.class).queryParam("t", optout.getToken()).build();
                        optout.setUrl(optoutUri.toString());
                        notification.notify(event.withData(optout));
                    } catch (EntityNotFoundException | IllegalIdentifierException e) {
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
    public CollectionPage<Record> listRecords(RecordFilter filter) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Listing records");
        if (!authentication.isConnectedIdentifierOperator() && !filter.getSubject().equals(authentication.getConnectedIdentifier())) {
            throw new AccessDeniedException("You must be operator to perform sur records search");
        }
        filter.setOwner(config.owner());
        PanacheQuery<Record> query;
        Sort sort = SortUtil.fromFilter(filter);
        if (sort != null) {
            query = Record.find(filter.getQueryString(), sort, filter.getQueryParams());
        } else {
            query = Record.find(filter.getQueryString(), filter.getQueryParams());
        }
        return PageUtil.paginateQuery(query, filter);
    }

    @Override
    public Map<String, List<Record>> listSubjectRecords(String subject) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Listing records for subject");
        if (!authentication.isConnectedIdentifierOperator() && !subject.equals(authentication.getConnectedIdentifier())) {
            throw new AccessDeniedException("You must be operator to perform sur records search");
        }
        RecordFilter filter = new RecordFilter();
        filter.setOwner(config.owner());
        filter.setStatus(Collections.singletonList(Record.Status.COMMITTED));
        filter.setSubject(subject);
        Stream<Record> records = Record.stream(filter.getQueryString(), filter.getQueryParams());
        Map<String, List<Record>> result = records.collect(Collectors.groupingBy(record -> record.bodyKey));
        result.entrySet().stream().forEach(entry -> {
            Collections.sort(entry.getValue());
            statusFilterChain.apply(entry.getValue());
        });
        return result;
        //TODO We could create a subject based record cache avoiding checking all of that each request
        //  cache will be invalidated :
        //  - when a consent is submitted for the subject
        //  - when a model is modified
    }

    @Override
    public List<String> findSubjects(String name) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Searching subjects");
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to search for subjects");
        }
        List<Subject> result = Subject.list("owner = ?1 and name like ?2", config.owner(), "%" + name + "%");
        return result.stream().map(s -> s.name).collect(Collectors.toList());
    }

    @Override
    public List<Record> systemFindRecordsForContext(ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Listing records");
        RecordFilter filter = new RecordFilter();
        filter.setOwner(config.owner());
        filter.setSubject(ctx.getSubject());
        filter.setStatus(Collections.singletonList(Record.Status.COMMITTED));
        if (ctx.getHeader() != null && !ctx.getHeader().isEmpty()) {
            filter.setHeaders(ModelVersion.SystemHelper.findActiveSerialsForKey(config.owner(), ctx.getHeader()));
        }
        if (ctx.getFooter() != null && !ctx.getFooter().isEmpty()) {
            filter.setFooters(ModelVersion.SystemHelper.findActiveSerialsForKey(config.owner(), ctx.getFooter()));
        }
        filter.setElements(ctx.getElements().stream().flatMap(e -> ModelVersion.SystemHelper.findActiveSerialsForKey(config.owner(), e).stream()).collect(Collectors.toList()));
        List<Record> records = Record.find(filter.getQueryString(), filter.getQueryParams()).list();
        List<Record> latest = new ArrayList<>();
        for (String element: ctx.getElements()) {
            records.stream().filter(record -> record.bodyKey.equals(element)).sorted().findFirst().ifPresent(r -> latest.add(r));
        }
        return latest;
    }

    //INTERN

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
            if (!Subject.existsForOwner(config.owner(), ctx.getSubject())) {
                Subject subject = new Subject();
                subject.owner = config.owner();
                subject.name = ctx.getSubject();
                Subject.persist(subject);
            }
            List<Record> records = new ArrayList<>();
            for (Map.Entry<String, String> value : values.entrySet()) {
                try {
                    ConsentElementIdentifier bodyId = ConsentElementIdentifier.deserialize(value.getKey());
                    Record record = new Record();
                    record.transaction = transaction;
                    record.subject = ctx.getSubject();
                    record.owner = config.owner();
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
                    record.author = !StringUtils.isEmpty(ctx.getAuthor()) ? ctx.getAuthor() : config.owner();
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
                    header = (Header) ModelVersion.SystemHelper.findModelVersionForSerial(headId.getSerial(), false).getData(ctx.getLocale());
                }
                Footer footer = null;
                if (footId != null) {
                    footer = (Footer) ModelVersion.SystemHelper.findModelVersionForSerial(footId.getSerial(), false).getData(ctx.getLocale());
                }
                Map<Treatment, Record> trecords = new HashMap<>();
                records.stream().filter(r -> r.type.equals(Treatment.TYPE)).forEach(r -> {
                    try {
                        Treatment t = (Treatment) ModelVersion.SystemHelper.findModelVersionForSerial(r.bodySerial, false).getData(ctx.getLocale());
                        trecords.put(t, r);
                    } catch (EntityNotFoundException | ModelDataSerializationException e) {
                        //
                    }
                });
                receipt = Receipt.build(transaction, config.processor(), ZonedDateTime.ofInstant(now, ZoneId.of("UTC")), ctx, header, footer, trecords);
                LOGGER.log(Level.INFO, "Receipt XML: " + receipt.toXml());
                //TODO Sign the receipt...
                byte[] xml = receipt.toXmlBytes();
                for (ReceiptStore store : stores) {
                    store.put(receipt.getTransaction(), xml);
                }
            }
            return receipt;
        } catch (EntityNotFoundException | ModelDataSerializationException | JAXBException | ReceiptAlreadyExistsException | ReceiptStoreException | IllegalIdentifierException | DatatypeConfigurationException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

}
