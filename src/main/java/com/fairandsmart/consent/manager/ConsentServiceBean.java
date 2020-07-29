package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigInteger;
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
    TokenService tokenService;

    @Inject
    ReceiptStore store;

    @ConfigProperty(name = "consent.processor")
    String processor;

    @PersistenceContext
    EntityManager entityManager;

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
        return tokenService.generateToken(ctx);
    }

    @Override
    public ConsentForm generateForm(String token, String subject) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException {
        //TODO :
        // 1. Load existing records for elements of this context (applying models invalidation strategy)
        //    Adapt ConsentForm to include existing values for each elements
        // 2. According to the ConsentContext requisite adopt the correct behaviour for display or not the form or parts of the form
        // 3. If form has to be displayed, load all models to populate
        // 4. Generate a new submission token and populate the form
        LOGGER.log(Level.INFO, "Generating consent form");
        try {
            ConsentContext ctx = (ConsentContext) tokenService.readToken(token);
            if (ctx.getSubject() == null || ctx.getSubject().isEmpty()) {
                if (subject != null && !subject.isEmpty()) {
                    ctx.setSubject(subject);
                } else if (!ctx.isPreview()) {
                    throw new ConsentServiceException("Subject is empty");
                }
            }

            List<Record> previousConsents = new ArrayList<>();
            if (!ctx.isConditions()) {
                previousConsents = findRecordsForContext(ctx);
            }

            ConsentForm form = new ConsentForm();
            form.setLocale(ctx.getLocale());
            form.setOrientation(ctx.getOrientation());
            form.setPreview(ctx.isPreview());
            form.setConditions(ctx.isConditions());

            if (ctx.getHeader() != null && !ctx.getHeader().isEmpty()) {
                ModelVersion header = this.systemFindActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
                form.setHeader(header);
                ctx.setHeader(header.getIdentifier().serialize());
            }

            List<String> elementsIdentifiers = new ArrayList<>();
            for (String key : ctx.getElements()) {
                ModelVersion element = this.systemFindActiveVersionByKey(ctx.getOwner(), key);
                previousConsents.stream().filter(r -> r.bodyKey.equals(key)).findFirst().ifPresent(r -> form.addPreviousValue(element.serial, r.value));
                if (ctx.getFormType().equals(ConsentContext.FormType.FULL) || !form.getPreviousValues().containsKey(element.serial)) {
                    form.addElement(element);
                    elementsIdentifiers.add(element.getIdentifier().serialize());
                }
            }
            ctx.setElements(elementsIdentifiers);

            if (ctx.getFooter() != null && !ctx.getFooter().isEmpty()) {
                ModelVersion footer = this.systemFindActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
                form.setFooter(footer);
                ctx.setFooter(footer.getIdentifier().serialize());
            }

            if (ctx.getTheme() != null && !ctx.getTheme().isEmpty()) {
                ModelVersion theme = this.systemFindActiveVersionByKey(ctx.getOwner(), ctx.getTheme());
                form.setTheme(theme);
                ctx.setTheme(theme.getIdentifier().serialize());
            }

            form.setToken(tokenService.generateToken(ctx));
            return form;
        } catch (TokenServiceException | ConsentServiceException e) {
            throw new ConsentServiceException("Unable to generate consent form", e);
        }
    }

    @Override
    public ConsentForm generateThemePreview(ConsentForm.Orientation orientation, String locale) throws ModelDataSerializationException {
        ConsentForm form = new ConsentForm();
        form.setLocale(locale);
        form.setOrientation(orientation);
        form.setPreview(true);
        form.setConditions(false);
        form.setToken("PREVIEW");

        Header lipsumHeader = new Header();
        lipsumHeader.setLogoAltText("Quisque rutrum, velit id congue facilisis");
        lipsumHeader.setLogoPath("/assets/img/themes/preview_logo.png");
        lipsumHeader.setTitle("Mauris auctor orci vestibulum sapien");
        lipsumHeader.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed et libero eu augue placerat bibendum auctor ac elit. Duis imperdiet metus felis, laoreet porta dolor faucibus nec. Donec in diam tincidunt, commodo erat sit amet, congue ipsum. Nullam venenatis ipsum nibh, vel viverra tortor venenatis ut. Morbi id urna quis lorem porta sodales vitae at leo. Praesent sed lorem rutrum, facilisis turpis at, viverra lorem. Mauris lorem justo, vulputate quis ipsum id, vestibulum efficitur sapien.");
        lipsumHeader.setPrivacyPolicyUrl("https://www.lipsum.com/feed/html");
        lipsumHeader.setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM.name());
        lipsumHeader.setDataController(new Controller().withCompany("Company").withName("John Doe").withAddress("Paris").withEmail("john.doe@company").withPhoneNumber("0123456789").withActingBehalfCompany(true));
        lipsumHeader.setJurisdiction("Duis sed lorem");
        lipsumHeader.setScope("Pellentesque a ex luctus");
        lipsumHeader.setShortNoticeLink("https://www.lipsum.com/feed/html");
        lipsumHeader.setShowCollectionMethod(true);
        lipsumHeader.setShowDataController(true);
        lipsumHeader.setShowJurisdiction(true);
        lipsumHeader.setShowScope(true);
        lipsumHeader.setShowShortNoticeLink(true);
        form.setHeader(generateVersionForPreview(locale, lipsumHeader));

        Treatment lipsumTreatment = new Treatment();
        lipsumTreatment.setTreatmentTitle("Donec eu ex nunc");
        lipsumTreatment.setDataTitle("Ut laoreet egestas tempus");
        lipsumTreatment.setDataBody("Nunc fringilla eros nec elit pellentesque mattis. In magna lacus, faucibus non augue vel, aliquet mollis arcu. Donec nulla nisl, ullamcorper et augue eget, dapibus feugiat nisl. In vitae ligula commodo, blandit massa sit amet, tincidunt est. Nullam ut sapien ut lacus iaculis tincidunt. Ut et pellentesque quam, at molestie orci.");
        lipsumTreatment.setRetentionTitle("Pellentesque maximus congue ultricies");
        lipsumTreatment.setRetentionBody("Donec dignissim cursus euismod. Donec luctus ante sed nibh dictum, ut imperdiet mauris eleifend. Pellentesque nec felis at tellus porta dapibus et sit amet lacus. In mauris tellus, venenatis euismod ipsum vel, blandit ornare neque. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Praesent aliquam, libero eget tristique consequat, massa leo pharetra nisl, vel semper felis est id metus.");
        lipsumTreatment.setUsageTitle("Fusce molestie nisi sed molestie tincidunt");
        lipsumTreatment.setUsageBody("Sed suscipit nec orci ac fermentum. Etiam ut urna diam. In rutrum tortor magna, non auctor massa interdum id. Maecenas a imperdiet turpis. Pellentesque ac imperdiet lorem. Integer porta nulla nec interdum facilisis. Phasellus sit amet rutrum mauris. Duis et tellus cursus, rhoncus tortor quis, tristique tellus. Nam ut dui id massa rhoncus pharetra rhoncus pharetra lacus. Phasellus vitae sagittis neque.");
        lipsumTreatment.addPurpose(Treatment.Purpose.CONSENT_CORE_SERVICE);
        lipsumTreatment.addPurpose(Treatment.Purpose.CONSENT_THIRD_PART_SHARING);
        lipsumTreatment.addPurpose(Treatment.Purpose.CONSENT_MARKETING);
        lipsumTreatment.setContainsMedicalData(true);
        lipsumTreatment.setContainsSensitiveData(true);
        lipsumTreatment.setDataController(new Controller().withCompany("Company").withName("Jane Doe").withAddress("Paris").withEmail("jane.doe@company").withPhoneNumber("0123456789").withActingBehalfCompany(true));
        lipsumTreatment.setShowDataController(true);
        lipsumTreatment.addThirdParty(new NameValuePair("Quisque eu tincidunt", "Aliquam varius lectus id facilisis commodo. Suspendisse hendrerit malesuada nisl, in egestas leo venenatis a. Praesent at elit non nisl condimentum rhoncus."));
        lipsumTreatment.addThirdParty(new NameValuePair("Vivamus quis", "Suspendisse pretium tincidunt turpis et tempor. Maecenas blandit in magna id rutrum. Nullam eu ligula ex."));
        form.addElement(generateVersionForPreview(locale, lipsumTreatment));

        Treatment lipsumTreatment2 = new Treatment();
        lipsumTreatment2.setTreatmentTitle("Cras sed rutrum dui, viverra porttitor mauris");
        lipsumTreatment2.setDataTitle("Integer ultrices augue non lorem euismod, malesuada tempor libero interdum");
        lipsumTreatment2.setDataBody("Nulla quis placerat diam. Vivamus turpis nulla, sodales at luctus sed, aliquet quis odio. Sed sit amet bibendum quam. Vestibulum nec nisl sodales libero malesuada auctor a at elit. Maecenas in finibus ante, ut egestas orci. Maecenas iaculis sagittis orci. Sed eget nulla nulla. Vestibulum eros arcu, lobortis accumsan sem rhoncus, mollis commodo odio.");
        lipsumTreatment2.setRetentionTitle("Sed non urna eget lectus consectetur sodales");
        lipsumTreatment2.setRetentionBody("Donec condimentum dictum erat a sollicitudin. Cras vitae lectus fringilla leo sagittis semper. Vivamus vulputate, risus ac malesuada pulvinar, nulla dui pharetra nibh, eu pretium ante urna sit amet nibh. Nunc urna turpis, bibendum eu volutpat sed, maximus iaculis enim. Nullam fringilla a velit eget tempus. Etiam sed ex nec ligula mattis aliquet ut sed nibh.");
        lipsumTreatment2.setUsageTitle("Fusce sodales ligula non lorem volutpat interdum");
        lipsumTreatment2.setUsageBody("Vestibulum ac augue et sapien accumsan viverra. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Aenean accumsan quam non massa luctus, a condimentum ipsum bibendum. Curabitur rutrum mauris et odio mollis faucibus. Duis fringilla blandit sem, non vulputate mauris ullamcorper ac. Sed dignissim id erat congue dignissim. Nulla pulvinar porttitor arcu sit amet dapibus. Sed interdum venenatis neque, eu commodo lorem porta ut.");
        lipsumTreatment2.addPurpose(Treatment.Purpose.CONSENT_RESEARCH);
        lipsumTreatment2.setContainsMedicalData(true);
        lipsumTreatment2.setContainsSensitiveData(true);
        lipsumTreatment2.setDataController(new Controller().withCompany("Company").withName("Jack Doe").withAddress("Paris").withEmail("jack.doe@company").withPhoneNumber("0123456789").withActingBehalfCompany(true));
        lipsumTreatment2.setShowDataController(true);
        lipsumTreatment2.addThirdParty(new NameValuePair("Aliquam", "Nullam in vulputate risus. Praesent sed tempus turpis, non lacinia tellus. Maecenas non mi dui. Proin imperdiet consectetur mi ornare porttitor. In rutrum ipsum eu mattis pellentesque."));
        form.addElement(generateVersionForPreview(locale, lipsumTreatment2));

        Footer lipsumFooter = new Footer();
        lipsumFooter.setBody("Nulla in aliquet elit. Quisque ultricies hendrerit mi accumsan lobortis. Mauris elementum ipsum vitae euismod accumsan. Duis eget placerat mi, at eleifend mauris. Nulla velit libero, cursus vitae lacinia at, mattis eget sem. Praesent at ligula lacus. Donec a ligula vel odio accumsan commodo. Aenean varius diam sed turpis volutpat ornare.");
        lipsumFooter.setShowAcceptAll(true);
        form.setFooter(generateVersionForPreview(locale, lipsumFooter));

        return form;
    }

    @Override
    @Transactional
    public Receipt submitConsent(String token, MultivaluedMap<String, String> values) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, InvalidConsentException {
        LOGGER.log(Level.INFO, "Submitting consent");
        try {
            ConsentContext ctx = (ConsentContext) tokenService.readToken(token);
            if (ctx.getSubject() == null || ctx.getSubject().isEmpty()) {
                throw new ConsentServiceException("Subject is empty");
            }

            Map<String, String> valuesMap = new HashMap<>();
            for (MultivaluedMap.Entry<String, List<String>> value : values.entrySet()) {
                valuesMap.put(value.getKey(), value.getValue().get(0));
            }
            return this.saveConsent(ctx, valuesMap, "");
        } catch (TokenServiceException | ConsentServiceException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    @Override
    public List<Record> findRecordsForContext(ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Listing records");
        List<Record> records = new ArrayList<>();

        try {
            ModelVersion headerVersion = null;
            if (ctx.getHeader() != null && !ctx.getHeader().isEmpty()) {
                headerVersion = systemFindActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
            }
            ModelVersion footerVersion = null;
            if (ctx.getFooter() != null && !ctx.getFooter().isEmpty()) {
                footerVersion = systemFindActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
            }
            for (String elementKey : ctx.getElements()) {
                ModelVersion elementVersion = systemFindActiveVersionByKey(ctx.getOwner(), elementKey);
                //TODO Maybe add also a condition on the status (COMMITTED)
                Record.find(
                        "subject = ?1 and headSerial in ?2 and bodySerial in ?3 and footSerial in ?4 and expirationTimestamp >= ?5",
                        Sort.by("creationTimestamp", Sort.Direction.Descending),
                        ctx.getSubject(),
                        headerVersion.getSerials(),
                        elementVersion.getSerials(),
                        footerVersion != null ? footerVersion.getSerials() : new ArrayList<>(),
                        System.currentTimeMillis()
                ).stream().findFirst().ifPresent(r -> records.add((Record) r));
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
        CollectionPage<Record> result = new CollectionPage<>();
        result.setValues(query.page(Page.of(filter.getPage(), filter.getSize())).list());
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(query.count());
        return result;
    }

    @Override
    public CollectionPage<UserRecord> listUserRecords(UserRecordFilter filter) {
        LOGGER.log(Level.INFO, "Listing user records for " + filter.getUser());
        @SuppressWarnings("unchecked")
        List<Object[]> rawResults = entityManager.createNativeQuery(
                "SELECT entry.key as bodyKey, subquery.owner, subquery.subject, subquery.creationTimestamp, " +
                        "subquery.expirationTimestamp, entry.type, subquery.value, subquery.status, subquery.collectionMethod, " +
                        "subquery.comment, subquery.headKey, subquery.footKey " +
                        "FROM ModelEntry entry LEFT JOIN (SELECT record1.* FROM Record record1 LEFT JOIN Record record2 " +
                        "ON (record1.owner = record2.owner AND record1.subject = record2.subject " +
                        "AND record1.bodyKey = record2.bodyKey AND record1.creationTimestamp < record2.creationTimestamp) " +
                        "WHERE record2.owner IS NULL AND record1.owner = ?1 " +
                        "AND record1.subject = ?2 AND (record1.type = 'treatment' OR record1.type = 'conditions') " +
                        "ORDER BY record1.bodyKey, record1.creationTimestamp DESC) AS subquery " +
                        "ON entry.key = subquery.bodyKey WHERE entry.owner = ?1 AND (entry.type = 'treatment' OR entry.type = 'conditions') " +
                        filter.getSQLOptionalFilters() + " ORDER BY " + filter.getSQLOrder())
                .setParameter(1, authentication.getConnectedIdentifier())
                .setParameter(2, filter.getUser())
                .getResultList();

        List<UserRecord> userRecords = new ArrayList<>();
        int index = filter.getSize() * filter.getPage();
        int max = filter.getSize() * (filter.getPage() + 1);
        while (index < rawResults.size() && index < max) {
            Object[] e = rawResults.get(index);
            UserRecord record = new UserRecord();
            record.setBodyKey((String) e[0]);
            record.setOwner((String) e[1]);
            record.setSubject((String) e[2]);
            record.setCreationTimestamp(e[3] != null ? ((BigInteger) e[3]).longValue() : 0);
            record.setExpirationTimestamp(e[4] != null ? ((BigInteger) e[4]).longValue() : 0);
            record.setType((String) e[5]);
            record.setValue((String) e[6]);
            record.setStatus((String) e[7]);
            record.setCollectionMethod((String) e[8]);
            record.setComment((String) e[9]);
            record.setHeaderKey((String) e[10]);
            record.setFooterKey((String) e[11]);
            userRecords.add(record);
            index++;
        }

        CollectionPage<UserRecord> userRecordsCollection = new CollectionPage<>();
        userRecordsCollection.setValues(userRecords);
        userRecordsCollection.setPage(filter.getPage());
        userRecordsCollection.setPageSize(filter.getSize());
        userRecordsCollection.setTotalPages((int) Math.ceil((double) (rawResults.size()) / (double) (filter.getSize())));
        userRecordsCollection.setTotalCount(rawResults.size());
        return userRecordsCollection;
    }

    @Override
    @Transactional
    public Receipt createOperatorRecords(String token, Map<String, String> values, String comment) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, InvalidConsentException {
        LOGGER.log(Level.INFO, "Creating record for operator");
        try {
            ConsentContext ctx = (ConsentContext) tokenService.readToken(token);

            ModelVersion headerVersion = this.systemFindActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
            ctx.setHeader(headerVersion.getIdentifier().serialize());
            ModelVersion footerVersion = this.systemFindActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
            ctx.setFooter(footerVersion.getIdentifier().serialize());
            List<String> newElements = new ArrayList<>();
            for (String element : ctx.getElements()) {
                ModelVersion elementVersion = this.systemFindActiveVersionByKey(ctx.getOwner(), element);
                newElements.add(elementVersion.getIdentifier().serialize());
            }
            ctx.setElements(newElements);

            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("header", headerVersion.getIdentifier().serialize());
            valuesMap.put("footer", footerVersion.getIdentifier().serialize());
            for (Map.Entry<String, String> value : values.entrySet()) {
                ModelVersion bodyVersion = this.systemFindActiveVersionByKey(ctx.getOwner(), value.getKey());
                valuesMap.put(bodyVersion.getIdentifier().serialize(), value.getValue());
            }

            return this.saveConsent(ctx, valuesMap, comment);
        } catch (TokenServiceException | ConsentServiceException | EntityNotFoundException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
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
        if (ctx.getHeader() == null && values.containsKey("header")) {
            throw new InvalidConsentException("submitted header incoherency, expected: null got: " + values.get("header"));
        }
        if (ctx.getHeader() != null && !ctx.getHeader().isEmpty() && (!values.containsKey("header") || !values.get("header").equals(ctx.getHeader()))) {
            throw new InvalidConsentException("submitted header incoherency, expected: " + ctx.getHeader() + " got: " + values.get("header"));
        }
        if (ctx.getFooter() == null && values.containsKey("footer")) {
            throw new InvalidConsentException("submitted footer incoherency, expected: null got: " + values.get("footer"));
        }
        if (ctx.getFooter() != null && !ctx.getFooter().isEmpty() && (!values.containsKey("footer") || !values.get("footer").equals(ctx.getFooter()))) {
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
            if (ctx.getHeader() != null && !ctx.getHeader().isEmpty()) {
                headId = ConsentElementIdentifier.deserialize(ctx.getHeader());
            }
            ConsentElementIdentifier footId = null;
            if (ctx.getFooter() != null && !ctx.getFooter().isEmpty()) {
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
                    record.headSerial = headId.getSerial();
                    record.bodySerial = bodyId.getSerial();
                    record.footSerial = footId != null ? footId.getSerial() : "";
                    record.headKey = headId.getKey();
                    record.bodyKey = bodyId.getKey();
                    record.footKey = footId.getKey();
                    record.serial = headId.getSerial() + "." + bodyId.getSerial() + "." + footId.getSerial();
                    record.value = value.getValue();
                    record.creationTimestamp = now.toEpochMilli();
                    record.expirationTimestamp = now.plusMillis(ctx.getValidityInMillis()).toEpochMilli();
                    record.status = Record.Status.COMMITTED;
                    record.collectionMethod = ctx.getCollectionMethod();
                    record.author = ctx.getAuthor() != null && !ctx.getAuthor().isEmpty() ? ctx.getAuthor() : ctx.getOwner();
                    record.comment = comment;
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
                        Treatment t = (Treatment) systemFindModelVersionForSerial(r.bodySerial).getData(ctx.getLocale());
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
