package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 *
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 *
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.util.Base58;
import com.fairandsmart.consent.common.util.PageUtil;
import com.fairandsmart.consent.common.util.SortUtil;
import com.fairandsmart.consent.manager.cache.PreviewCache;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.exception.*;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.model.*;
import com.fairandsmart.consent.manager.render.*;
import com.fairandsmart.consent.manager.rule.BasicRecordStatusRuleChain;
import com.fairandsmart.consent.manager.store.LocalFolderReceiptStore;
import com.fairandsmart.consent.manager.store.ReceiptAlreadyExistsException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.manager.store.ReceiptStoreException;
import com.fairandsmart.consent.notification.NotificationService;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.entity.EventArgs;
import com.fairandsmart.consent.notification.entity.EventType;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.StartupEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fairandsmart.consent.manager.entity.ModelEntry.DEFAULT_BRANCHE;

@ApplicationScoped
public class ConsentServiceBean implements ConsentService {

    private static final Logger LOGGER = Logger.getLogger(ConsentService.class.getName());
    private static final String NEW_VERSION_UUID = "11111111-9999-1111-9999-111111111111";

    @Inject
    AuthenticationService authentication;

    @Inject
    SerialGenerator generator;

    @Inject
    TokenService tokenService;

    @Inject
    LocalFolderReceiptStore store;

    @Inject
    NotificationService notification;

    @Inject
    MainConfig config;

    /* Keys are ModelEntries ids since only one version per entry can be previewed. It helps clearing the cache when an entry is deleted. */
    @Inject
    PreviewCache previewCache;

    @Inject
    BasicRecordStatusRuleChain recordStatusChain;

    @Inject
    Instance<ReceiptRenderer> renderers;

    /* MODELS MANAGEMENT */

    @Override
    public CollectionPage<ModelEntry> listEntries(ModelFilter filter) {
        LOGGER.log(Level.FINE, "Listing models entries");
        PanacheQuery<ModelEntry> query;
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
    public ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException, ConsentManagerException {
        LOGGER.log(Level.INFO, "Creating new entry");
        authentication.ensureConnectedIdentifierIsAdmin();
        if (key == null || key.isEmpty()) {
            throw new ConsentManagerException("Cannot create a model entry with an empty key");
        } else if (ModelEntry.isKeyAlreadyExists(key)) {
            throw new EntityAlreadyExistsException("A model entry already exists with key: " + key);
        }
        long now = System.currentTimeMillis();
        ModelEntry entry = new ModelEntry();
        entry.type = type;
        entry.key = key;
        entry.name = name;
        entry.description = description;
        entry.branches = DEFAULT_BRANCHE;
        entry.author = authentication.getConnectedIdentifier();
        entry.creationDate = now;
        entry.modificationDate = now;
        entry.status = ModelEntry.Status.INACTIVE;
        entry.persist();

        this.notification.publish(EventType.MODEL_CREATE, ModelEntry.class.getName(), entry.id, entry.author, EventArgs.build("name", name).addArg("description", description).addArg("type", type));
        return entry;
    }

    @Override
    public ModelEntry getEntry(String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.FINE, "Getting entry for id: " + id);
        authentication.ensureConnectedIdentifierIsOperator();
        Optional<ModelEntry> optional = ModelEntry.findByIdOptional(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + id));
    }

    @Override
    public ModelEntry findEntryForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding entry for key: " + key);
        Optional<ModelEntry> optional = ModelEntry.find("key = ?1", key).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for key: " + key));
    }

    @Override
    @Transactional
    public ModelEntry updateEntry(String id, String name, String description) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.FINE, "Updating entry for id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelEntry> optional = ModelEntry.findByIdOptional(id);
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + id));
        entry.name = name;
        entry.description = description;
        entry.modificationDate = System.currentTimeMillis();
        entry.persist();

        this.notification.publish(EventType.MODEL_UPDATE, ModelEntry.class.getName(), entry.id, entry.author, EventArgs.build("name", name).addArg("description", description));
        return entry;
    }

    @Override
    @Transactional
    public void deleteEntry(String id) throws ConsentManagerException, EntityNotFoundException, InvalidStatusException {
        LOGGER.log(Level.FINE, "Deleting entry with id: {0}", id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelEntry> optional = ModelEntry.findByIdOptional(id);
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + id));
        List<Record> records = Record.list(entry.type.equals(BasicInfo.TYPE) ? "infoKey" : "bodyKey", entry.key);
        if (!records.isEmpty()) {
            LOGGER.log(Level.FINE, "Entry with id: {0} has records, marking it as DELETED", id);
            if (entry.status == ModelEntry.Status.ACTIVE) {
                this.updateVersionStatus(this.findActiveVersionForEntry(entry.id).id, ModelVersion.Status.ARCHIVED);
            }
            entry.status = ModelEntry.Status.DELETED;
            entry.modificationDate = System.currentTimeMillis();
            entry.persist();
            LOGGER.log(Level.FINE, "Invalidating records for entry with id: {0}", id);
            for (Record record : records) {
                record.state = Record.State.DELETED;
                record.persist();
            }
        } else {
            LOGGER.log(Level.FINE, "Entry with id: {0} has no record, deleting it from database", id);
            List<ModelVersion> versions = ModelVersion.find("entry.id = ?1", id).list();
            versions.forEach(v -> ModelVersion.deleteById(v.id));
            ModelEntry.deleteById(id);
        }
        previewCache.remove(id);
        this.notification.publish(EventType.MODEL_DELETE, ModelEntry.class.getName(), entry.id, entry.author);
    }

    @Override
    @Transactional
    public ModelVersion createVersion(String entryId, String defaultLanguage, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.FINE, "Creating new version for entry with id: " + entryId);
        authentication.ensureConnectedIdentifierIsAdmin();
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelEntry> optional = ModelEntry.find("id = ?1", entryId).singleResultOptional();
        ModelEntry entry = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + entryId));
        if (data.values().stream().anyMatch(d -> !d.getType().equals(entry.type))) {
            throw new ConsentManagerException("One content data type does not belongs to entry type: " + entry.type);
        }
        Optional<ModelVersion> voptional = ModelVersion.find("entry.id = ?1 and child = ?2", entryId, "").singleResultOptional();
        ModelVersion latest = voptional.orElse(null);
        try {
            long now = System.currentTimeMillis();
            if (latest == null) {
                LOGGER.log(Level.FINE, "No existing version found, creating first one");
                latest = new ModelVersion();
                latest.entry = entry;
                latest.author = connectedIdentifier;
                latest.branches = DEFAULT_BRANCHE;
                latest.creationDate = now;
                latest.status = ModelVersion.Status.DRAFT;
                latest.serial = generator.next(ModelVersion.class.getName());
                latest.type = ModelVersion.Type.MINOR;
            } else if (latest.status.equals(ModelVersion.Status.DRAFT)) {
                throw new ConsentManagerException("A draft version already exists, unable to create new one");
            } else {
                LOGGER.log(Level.FINE, "Latest version found, creating new one");
                ModelVersion newversion = new ModelVersion();
                newversion.entry = latest.entry;
                newversion.author = connectedIdentifier;
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
            this.notification.publish(EventType.MODEL_VERSION_CREATE, ModelEntry.class.getName(), entry.id, entry.author, EventArgs.build("serial", latest.serial));
            return this.updateVersionContent(latest, data, defaultLanguage, connectedIdentifier);
        } catch (SerialGeneratorException | ModelDataSerializationException ex) {
            throw new ConsentManagerException("unable to create new version", ex);
        }
    }

    @Override
    public ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding active version for entry with key: " + key);
        return ModelVersion.SystemHelper.findActiveVersionByKey(key);
    }

    @Override
    public ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding active version for entry with id: " + entryId);
        return ModelVersion.SystemHelper.findActiveVersionByEntryId(entryId);
    }

    @Override
    public ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding latest version for entry with key: " + key);
        Optional<ModelVersion> voptional = ModelVersion.find("entry.key = ?1 and child = ?2", key, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with key: " + key));
    }

    @Override
    public ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding latest version for entry with id: " + entryId);
        Optional<ModelVersion> voptional = ModelVersion.find("entry.id = ?1 and child = ?2", entryId, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with id: " + entryId));
    }

    @Override
    public ModelVersion getVersion(String id) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding version for id: " + id);
        Optional<ModelVersion> optional = ModelVersion.findByIdOptional(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for id: " + id));
    }

    @Override
    public ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding version for serial: " + serial);
        Optional<ModelVersion> optional = ModelVersion.find("serial = ?1", serial).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for serial: " + serial));
    }

    @Override
    public List<ModelVersion> getVersionHistoryForKey(String key) throws ConsentManagerException {
        LOGGER.log(Level.FINE, "Listing versions for entry with key: " + key);
        List<ModelVersion> versions = ModelVersion.find("entry.key = ?1", key).list();
        if (!versions.isEmpty()) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    public List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException {
        LOGGER.log(Level.FINE, "Listing versions for entry with id: " + entryId);
        List<ModelVersion> versions = ModelVersion.find("entry.id = ?1", entryId).list();
        if (!versions.isEmpty()) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    @Transactional
    public ModelVersion updateVersion(String id, String defaultLanguage, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.FINE, "Updating content for version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        String connectedIdentifier = authentication.getConnectedIdentifier();
        Optional<ModelVersion> voptional = ModelVersion.find("id = ?1", id).singleResultOptional();
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if (data.values().stream().anyMatch(d -> !d.getType().equals(version.entry.type))) {
            throw new ConsentManagerException("One content data type does not belongs to entry type: " + version.entry.type);
        }
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to update content for version that is not last one");
        }
        if (!version.status.equals(ModelVersion.Status.DRAFT)) {
            throw new ConsentManagerException("Unable to update type for version that is not DRAFT");
        }
        try {
            ModelVersion uversion = this.updateVersionContent(version, data, defaultLanguage, connectedIdentifier);
            this.notification.publish(EventType.MODEL_VERSION_UPDATE, ModelEntry.class.getName(), uversion.entry.id, uversion.entry.author, EventArgs.build("serial", uversion.serial));
            return uversion;
        } catch (ModelDataSerializationException ex) {
            throw new ConsentManagerException("Unable to serialise data", ex);
        }
    }

    @Override
    @Transactional
    public ModelVersion updateVersionType(String id, ModelVersion.Type type) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.FINE, "Updating type for version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelVersion> voptional = ModelVersion.findByIdOptional(id);
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to update type for version that is not last one");
        }
        if (!version.status.equals(ModelVersion.Status.DRAFT)) {
            throw new ConsentManagerException("Unable to update type for version that is not DRAFT");
        }
        long now = System.currentTimeMillis();
        version.type = type;
        version.modificationDate = now;
        version.persist();
        version.entry.modificationDate = now;
        version.entry.persist();
        this.notification.publish(EventType.MODEL_VERSION_UPDATE_TYPE, ModelEntry.class.getName(), version.entry.id, version.entry.author, EventArgs.build("serial", version.serial).addArg("type", type.toString()));
        return version;
    }

    @Override
    @Transactional
    public ModelVersion updateVersionStatus(String id, ModelVersion.Status status) throws ConsentManagerException, EntityNotFoundException, InvalidStatusException {
        LOGGER.log(Level.FINE, "Updating status for version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        long now = System.currentTimeMillis();
        Optional<ModelVersion> voptional = ModelVersion.findByIdOptional(id);
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to update status of a version that is not the latest");
        }
        if (status.equals(ModelVersion.Status.DRAFT)) {
            throw new InvalidStatusException("Unable to update a version to DRAFT status");
        } else if (status.equals(ModelVersion.Status.ACTIVE)) {
            if (!version.status.equals(ModelVersion.Status.DRAFT)) {
                throw new InvalidStatusException("Only DRAFT version can be set ACTIVE");
            } else {
                if (!version.parent.isEmpty()) {
                    ModelVersion parent = ModelVersion.findById(version.parent);
                    parent.modificationDate = now;
                    parent.status = ModelVersion.Status.ARCHIVED;
                    parent.persist();
                }
                version.status = ModelVersion.Status.ACTIVE;
                version.modificationDate = now;
                if (version.type.equals(ModelVersion.Type.MAJOR)) {
                    version.counterparts = "";
                }
                version.persist();
                version.entry.status = ModelEntry.Status.ACTIVE;
                version.entry.defaultLanguage = version.defaultLanguage;
                version.entry.availableLanguages = version.availableLanguages;
            }
        } else if (status.equals(ModelVersion.Status.ARCHIVED)) {
            if (!version.status.equals(ModelVersion.Status.ACTIVE)) {
                throw new InvalidStatusException("Only ACTIVE version can be set ARCHIVED");
            } else {
                version.modificationDate = now;
                version.status = ModelVersion.Status.ARCHIVED;
                version.persist();
                version.entry.status = ModelEntry.Status.INACTIVE;
                version.entry.defaultLanguage = null;
                version.entry.availableLanguages = "";
            }
        }
        version.entry.modificationDate = now;
        version.entry.persist();
        this.notification.publish(EventType.MODEL_VERSION_UPDATE_STATUS, ModelEntry.class.getName(), version.entry.id, version.entry.author, EventArgs.build("serial", version.serial).addArg("status", status.toString()));
        return version;
    }

    @Override
    public PreviewDto previewVersion(String entryId, String versionId, PreviewDto dto) throws AccessDeniedException, EntityNotFoundException, ModelDataSerializationException {
        ModelVersion version = previewCache.lookup(entryId);
        if (version == null) {
            if (versionId != null && !versionId.equals(NEW_VERSION_UUID)) {
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
            version.content.put(dto.getLanguage(), new ModelContent().withDataObject(dto.getData()));
            previewCache.put(entryId, version);
        } else {
            dto.setData(version.content.get(dto.getLanguage()).getDataObject());
        }
        return dto;
    }

    @Override
    @Transactional
    public void deleteVersion(String id) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.FINE, "Deleting version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelVersion> voptional = ModelVersion.findByIdOptional(id);
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to delete version that is not last one");
        }
        if (!version.status.equals(ModelVersion.Status.DRAFT)) {
            throw new ConsentManagerException("Unable to delete version that is not DRAFT");
        }
        version.entry.modificationDate = System.currentTimeMillis();
        version.entry.persist();
        Optional<ModelVersion> optionalParent = ModelVersion.findByIdOptional(version.parent);
        if (optionalParent.isPresent()) {
            ModelVersion parent = optionalParent.get();
            parent.child = "";
            parent.persist();
        }
        version.delete();
        this.notification.publish(EventType.MODEL_VERSION_DELETE, ModelEntry.class.getName(), version.entry.id, version.entry.author, EventArgs.build("serial", version.serial));
    }

    /* CONSENT MANAGEMENT */

    @Override
    public String buildToken(ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Building generate form token for context: " + ctx);
        if (ctx.getSubject() == null || ctx.getSubject().isEmpty()) {
            ctx.setSubject(authentication.getConnectedIdentifier());
        } else if (!ctx.getSubject().equals(authentication.getConnectedIdentifier()) && !authentication.isConnectedIdentifierApi()) {
            throw new AccessDeniedException("Only admin, operator or api can generate token for other identifier than connected one");
        }
        return tokenService.generateToken(ctx);
    }

    @Override
    public ConsentForm generateForm(String token) throws GenerateFormException, TokenExpiredException, InvalidTokenException, ConsentServiceException {
        LOGGER.log(Level.FINE, "Generating consent form");
        try {
            ConsentContext ctx = (ConsentContext) this.tokenService.readToken(token);
            //Assign transaction id
            ctx.setTransaction(Base58.encodeUUID(UUID.randomUUID().toString()));

            try {
                //Load layout model if exists
                if (StringUtils.isNotEmpty(ctx.getLayout())) {
                    ctx.setLayoutData((FormLayout) ModelVersion.SystemHelper.findActiveVersionByKey(extractElementKey(ctx.getLayout())).getData(ctx.getLanguage()));
                }
                if (ctx.getLayoutData() == null) {
                    throw new GenerateFormException(ctx, "Unable to generate consent form: layout data is null");
                }

                //Initialise form using context
                ConsentForm form = new ConsentForm(ctx);

                // Fetch elements from context
                List<String> elementsKeys = this.extractElementsKeys(ctx.getLayoutData().getElements());
                List<ModelVersion> elementsVersions = ModelVersion.SystemHelper.findActiveVersionsForKeys(elementsKeys);

                // Fetch previous records
                if (!ctx.isPreview()) {
                    final Map<String, Record> previousRecords = systemListValidRecords(ctx.getSubject(), ctx.getLayoutData().getInfo(), elementsKeys);
                    form.setPreviousValues(elementsVersions.stream().filter(version -> previousRecords.containsKey(version.entry.key)).collect(Collectors.toMap((v) -> v.serial, (v) -> previousRecords.get(v.entry.key).value)));
                }

                // Update form and context elements, infos, theme and notification
                final boolean existingElementsVisible = ctx.getLayoutData().isExistingElementsVisible();
                form.setElements(elementsVersions.stream().filter(version -> (existingElementsVisible || !form.getPreviousValues().containsKey(version.serial))).collect(Collectors.toList()));
                ctx.getLayoutData().setElements(form.getElements().stream().map(version -> version.getIdentifier().serialize()).collect(Collectors.toList()));
                if (StringUtils.isNotEmpty(ctx.getLayoutData().getInfo())) {
                    form.setInfo(ModelVersion.SystemHelper.findActiveVersionByKey(extractElementKey(ctx.getLayoutData().getInfo())));
                    ctx.getLayoutData().setInfo(form.getInfo().getIdentifier().serialize());
                }
                if (StringUtils.isNotEmpty(ctx.getLayoutData().getTheme())) {
                    form.setTheme(ModelVersion.SystemHelper.findActiveVersionByKey(extractElementKey(ctx.getLayoutData().getTheme())));
                    ctx.getLayoutData().setTheme(form.getTheme().getIdentifier().serialize());
                }
                if (StringUtils.isNotEmpty(ctx.getLayoutData().getNotification())) {
                    ModelVersion notification = ModelVersion.SystemHelper.findActiveVersionByKey(extractElementKey(ctx.getLayoutData().getNotification()));
                    ctx.getLayoutData().setNotification(notification.getIdentifier().serialize());
                }

                form.setToken(this.tokenService.generateToken(ctx));
                return form;
            } catch (EntityNotFoundException e) {
                throw new GenerateFormException(ctx, e.getMessage());
            }
        } catch (ModelDataSerializationException | TokenServiceException e) {
            throw new ConsentServiceException("Unable to generate consent form", e);
        }
    }

    @Override
    @Transactional
    public ConsentTransaction submitConsent(String token, MultivaluedMap<String, String> values) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, SubmitConsentException {
        LOGGER.log(Level.FINE, "Submitting consent");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        try {
            ConsentContext ctx = (ConsentContext) this.tokenService.readToken(token);

            try {
                if (StringUtils.isEmpty(ctx.getSubject())) {
                    throw new SubmitConsentException(ctx, null, "Subject is empty");
                }

                Map<String, String> valuesMap = new HashMap<>();
                for (MultivaluedMap.Entry<String, List<String>> value : values.entrySet()) {
                    valuesMap.put(value.getKey(), String.join(",", value.getValue()));
                }

                this.checkValuesCoherency(ctx, valuesMap);
                Instant now = Instant.now();

                Optional<ConsentElementIdentifier> infoIdOpt = ConsentElementIdentifier.deserialize(ctx.getLayoutData().getInfo());
                ConsentElementIdentifier infoId;
                BasicInfo info;
                if (infoIdOpt.isPresent()) {
                    infoId = infoIdOpt.get();
                    info = (BasicInfo) ModelVersion.SystemHelper.findModelVersionForSerial(infoId.getSerial(), false).getData(ctx.getLanguage());
                } else {
                    //If consent is submitted without basic info, we populate it with the first one.
                    ModelEntry infoEntry = ModelEntry.find("type", BasicInfo.TYPE).firstResult();
                    ModelVersion infoVersion = ModelVersion.SystemHelper.findActiveVersionByEntryId(infoEntry.id);
                    infoId = infoVersion.getIdentifier();
                    info = (BasicInfo) infoVersion.getData(ctx.getLanguage());
                }
                ctx.getLayoutData().setInfo(infoId.getKey());

                if (!Subject.exists(ctx.getSubject())) {
                    Subject subject = Subject.create(ctx.getSubject());
                    subject.persist();
                    this.notification.publish(EventType.SUBJECT_CREATE, Subject.class.getName(), subject.id, authentication.getConnectedIdentifier(), EventArgs.build("name", ctx.getSubject()));

                }
                String comment = values.containsKey("comment") ? valuesMap.get("comment") : "";

                List<Pair<Processing, Record>> trecords = new ArrayList<>();
                List<Record> records = ctx.getLayoutData().getElements().stream().map(ConsentElementIdentifier::deserialize).filter(Optional::isPresent).map(
                        opt -> Record.build(ctx, ctx.getTransaction(), authentication.getConnectedIdentifier(), now, infoId, opt.get(), valuesMap.get(opt.get().serialize()), comment)
                ).collect(Collectors.toList());
                for (Record record : records) {
                    ModelVersion version = ModelVersion.SystemHelper.findModelVersionForSerial(record.bodySerial, false);
                    if (Processing.TYPE.equals(version.entry.type)) {
                        Processing processing = (Processing) version.getData(ctx.getLanguage());
                        trecords.add(new ImmutablePair<>(processing, record));
                    }
                }
                Receipt receipt = Receipt.build(ctx.getTransaction(), config.processor(), ZonedDateTime.ofInstant(now, ZoneId.of("UTC")), ctx, info, trecords);
                ctx.setOrigin(ConsentContext.Origin.RECEIPT.getValue());
                String updateToken = tokenService.generateToken(ctx, Date.from(receipt.getExpirationDate().toInstant()));
                receipt.setUpdateUrl(config.publicUrl() + "/consents?t=" + updateToken);
                receipt.setUpdateUrlQrCode(generateQRCode(receipt.getUpdateUrl()));
                store.put(receipt);

                //Store records here to avoid previous error, depending on the receipt type (2PC) maybe set record status to PENDING...
                records.forEach(record -> record.persist());

                NotificationReport report;
                if (StringUtils.isNotEmpty(ctx.getNotificationRecipient()) && StringUtils.isNotEmpty(ctx.getLayoutData().getNotification())) {
                    report = new NotificationReport(ctx.getTransaction(), NotificationReport.Type.EMAIL, NotificationReport.Status.PENDING);
                } else {
                    report = new NotificationReport(ctx.getTransaction(), NotificationReport.Type.NONE, NotificationReport.Status.NONE);
                }
                this.notification.pushReport(report);

                Event<ConsentContext> event = new Event<ConsentContext>().addChannel(Event.NOTIFICATION_CHANNEL).withEventType(EventType.CONSENT_SUBMIT).withSourceType(ConsentContext.class.getName()).withSourceId(ctx.getTransaction()).withAuthor(connectedIdentifier).withData(ctx);
                this.notification.publish(event);

                return new ConsentTransaction(ctx.getTransaction());
            } catch (InvalidValuesException | EntityNotFoundException e) {
                //TODO Try to fix context with upgraded elements (separate EntityNotFoundException execption treatment)
                throw new SubmitConsentException(ctx, null, e);
            }
        } catch (TokenServiceException | DatatypeConfigurationException | ReceiptStoreException | ReceiptAlreadyExistsException | ModelDataSerializationException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    /* SUBJECTS */

    @Override
    public List<Subject> findSubjects(String name) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Searching subjects");
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to search for subjects");
        }
        return Subject.list("name like ?1", Sort.by("name", Sort.Direction.Descending), "%" + name + "%");
    }

    @Override
    public Subject getSubject(String name) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Getting subject for name: " + name);
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to search for subjects");
        }
        Optional<Subject> optional = Subject.find("name = ?1", name).singleResultOptional();
        if (optional.isPresent()) {
            return optional.get();
        } else {
            Subject subject = new Subject();
            subject.name = name;
            return subject;
        }
    }

    @Override
    @Transactional
    public Subject createSubject(String name, String email) throws ConsentManagerException, EntityAlreadyExistsException {
        LOGGER.log(Level.FINE, "Creating subject with name: " + name);
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to create subjects");
        }
        if (StringUtils.isEmpty(name)) {
            throw new ConsentManagerException("Subject name cannot be empty");
        }
        Optional<Subject> optional = Subject.find("name = ?1", name).singleResultOptional();
        if (optional.isPresent()) {
            throw new EntityAlreadyExistsException("This subject name already exists");
        }
        Instant now = Instant.now();
        Subject subject = new Subject();
        subject.name = name;
        subject.creationTimestamp = now.toEpochMilli();
        subject.emailAddress = email;
        subject.persist();

        this.notification.publish(EventType.SUBJECT_CREATE, Subject.class.getName(), subject.id, authentication.getConnectedIdentifier(), EventArgs.build("name", name).addArg("email", email));
        return subject;
    }

    @Override
    @Transactional
    public Subject updateSubject(String id, String email) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.FINE, "Updating subject with id: " + id);
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to update subjects");
        }
        Optional<Subject> optional = Subject.findByIdOptional(id);
        Subject subject = optional.orElseThrow(() -> new EntityNotFoundException("Unable to find a subject for id: " + id));
        subject.emailAddress = email;
        subject.persist();

        this.notification.publish(EventType.SUBJECT_UPDATE, Subject.class.getName(), subject.id, authentication.getConnectedIdentifier(), EventArgs.build("email", email));
        return subject;
    }

    /* RECORDS */

    @Override
    public Map<String, List<Record>> listSubjectRecords(RecordFilter filter) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Listing records for subject");
        if (!authentication.isConnectedIdentifierOperator() && !filter.getSubject().equals(authentication.getConnectedIdentifier())) {
            throw new AccessDeniedException("You must be operator to perform records search");
        }
        this.notification.publish(EventType.SUBJECT_LIST_RECORDS, Subject.class.getName(), filter.getSubject(), authentication.getConnectedIdentifier());
        return this.listRecordsWithStatus(filter);
    }

    @Override
    public Map<String, Record> systemListValidRecords(String subject, String infoKey, List<String> elementsKeys) {
        LOGGER.log(Level.FINE, "Listing valid records");
        RecordFilter filter = new RecordFilter();
        filter.setSubject(subject);
        filter.setStates(Collections.singletonList(Record.State.COMMITTED));
        if (infoKey != null && !infoKey.isEmpty()) {
            filter.setInfos(ModelVersion.SystemHelper.findActiveSerialsForKey(infoKey));
        }
        filter.setElements(elementsKeys.stream().flatMap(e -> ModelVersion.SystemHelper.findActiveSerialsForKey(e).stream()).collect(Collectors.toList()));
        Map<String, List<Record>> result = this.listRecordsWithStatus(filter);
        return result.entrySet().stream().filter(entry -> entry.getValue().stream().anyMatch(record -> record.status.equals(Record.Status.VALID)))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().filter(record -> record.status.equals(Record.Status.VALID)).findFirst().get()));
    }

    @Override
    public Map<Subject, Record> extractRecords(String key, String value, boolean regexpValue) throws AccessDeniedException, EntityNotFoundException {
        LOGGER.log(Level.FINE, "Extracting valid records with key: " + key + " and value: " + value + ((regexpValue) ? " (regexp)" : " (exact match)"));
        if (!authentication.isConnectedIdentifierAdmin()) {
            throw new AccessDeniedException("You must be admin to search subjects");
        }
        Map<Subject, Record> result = new HashMap<>();
        ModelEntry entry = findEntryForKey(key);
        List<String> serials = ModelVersion.SystemHelper.findActiveSerialsForKey(key);
        if (!serials.isEmpty()) {
            List<Subject> subjects = Subject.listAll();
            final Pattern pattern = regexpValue ? Pattern.compile(value) : null;
            RecordFilter filter = new RecordFilter();
            filter.setStates(Collections.singletonList(Record.State.COMMITTED));
            if (entry.type.equals(BasicInfo.TYPE)) {
                filter.setInfos(serials);
            } else {
                filter.setElements(serials);
            }
            subjects.forEach(subject -> {
                filter.setSubject(subject.name);
                List<Record> records = Record.list(filter.getQueryString(), filter.getQueryParams());
                recordStatusChain.apply(records);
                LOGGER.log(Level.FINE, "Rules applied on loaded records of subject : " + subject + ": " + records);
                records.stream()
                        .filter(r -> r.status.equals(Record.Status.VALID)
                                && (pattern != null ? pattern.matcher(r.value).matches() : r.value.equals(value)))
                        .findFirst()
                        .ifPresent(r -> result.put(subject, r));
            });
        }
        this.notification.publish(EventType.RECORD_EXTRACT, Record.class.getName(), key, authentication.getConnectedIdentifier());
        return result;
    }

    /* RECEIPTS */

    @Override
    public Receipt getReceipt(String token, String id) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException {
        LOGGER.log(Level.FINE, "Getting receipt for id: " + id);
        try {
            Receipt receipt = store.get(id);
            if (!authentication.getConnectedIdentifier().equals(receipt.getSubject()) && !authentication.isConnectedIdentifierOperator()) {
                if (StringUtils.isNotEmpty(token)) {
                    ConsentTransaction tx = (ConsentTransaction) tokenService.readToken(token);
                    if (!tx.getTransaction().equals(receipt.getTransaction())) {
                        throw new AccessDeniedException("Token transaction is not the same as receipt");
                    }
                } else {
                    throw new AccessDeniedException("You must be operator to retrieve receipts of other subjects");
                }
            }
            this.notification.publish(EventType.RECEIPT_READ, Receipt.class.getName(), id, authentication.getConnectedIdentifier());
            return receipt;
        } catch (ReceiptStoreException e) {
            throw new ConsentManagerException("Unable to read receipt from store", e);
        }
    }

    @Override
    public byte[] renderReceipt(String token, String id, String format, String themeKey) throws ReceiptNotFoundException, ConsentManagerException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.log(Level.FINE, "Rendering receipt for id: " + id + " and format: " + format + " and theme: " + themeKey);
        Receipt receipt = getReceipt(token, id);
        byte[] result =  this.internalRenderReceipt(receipt, format, themeKey);
        this.notification.publish(EventType.RECEIPT_READ, Receipt.class.getName(), id, authentication.getConnectedIdentifier(), EventArgs.build("format", format).addArg("theme", themeKey));
        return result;
    }

    @Override
    public byte[] systemRenderReceipt(String id, String format, String themeKey) throws ReceiptRendererNotFoundException, ReceiptStoreException, ReceiptNotFoundException, RenderingException, ModelDataSerializationException, EntityNotFoundException {
        LOGGER.log(Level.FINE, "##SYSTEM## Rendering receipt for id: " + id + " and format: " + format + " and theme: " + themeKey);
        Receipt receipt = store.get(id);
        byte[] result =  this.internalRenderReceipt(receipt, format, themeKey);
        this.notification.publish(EventType.RECEIPT_READ, Receipt.class.getName(), id, authentication.getConnectedIdentifier(), EventArgs.build("format", format).addArg("theme", themeKey));
        return result;
    }

    /* INTERNAL */

    private byte[] internalRenderReceipt(Receipt receipt, String format, String themeKey) throws ModelDataSerializationException, EntityNotFoundException, RenderingException, ReceiptRendererNotFoundException {
        Optional<ReceiptRenderer> renderer = renderers.stream().filter(r -> r.format().equals(format)).findFirst();
        if (renderer.isPresent()) {
            RenderableReceipt rreceipt = new RenderableReceipt(receipt, buildThemeInfo(themeKey, receipt.getLanguage()));
            return renderer.get().render(rreceipt);
        }
        throw new ReceiptRendererNotFoundException("unable to find a receipt renderer for format: " + format);
    }

    /* Extract keys from context elements whatever it is a full element identifier or a single key*/
    private List<String> extractElementsKeys(List<String> elements) {
        List<String> elementsKeys = new ArrayList<>();
        for (String element : elements) {
            elementsKeys.add(extractElementKey(element));
        }
        return elementsKeys;
    }

    private String extractElementKey(String element) {
        Optional<ConsentElementIdentifier> optional = ConsentElementIdentifier.deserialize(element);
        if (optional.isPresent()) {
            return optional.get().getKey();
        } else if (StringUtils.isNotEmpty(element)) {
            return element;
        } else {
            return null;
        }
    }

    private void checkValuesCoherency(ConsentContext ctx, Map<String, String> values) throws InvalidValuesException {
        if (ctx.getLayoutData().getInfo() == null && values.containsKey("info")) {
            throw new InvalidValuesException("submitted basic info incoherency", "null", values.get("info"));
        }
        if (StringUtils.isNotEmpty(ctx.getLayoutData().getInfo()) && (!values.containsKey("info") || !values.get("info").equals(ctx.getLayoutData().getInfo()))) {
            throw new InvalidValuesException("submitted basic info incoherency", ctx.getLayoutData().getInfo(), values.get("info"));
        }
        Map<String, String> submittedElementValues = values.entrySet().stream()
                .filter(e -> e.getKey().startsWith("element") && !e.getKey().endsWith("-optional"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Remove ignored optional preferences from context
        List<String> ignoredElements = values.keySet().stream()
                .filter(key -> key.endsWith("-optional")).map(key -> key.replace("-optional", ""))
                .filter(key -> !submittedElementValues.containsKey(key)).collect(Collectors.toList());
        ctx.getLayoutData().setElements(ctx.getLayoutData().getElements().stream().filter(e -> !ignoredElements.contains(e)).collect(Collectors.toList()));
        values.keySet().removeIf(key -> key.endsWith("-optional"));

        if (!new HashSet<>(ctx.getLayoutData().getElements()).equals(submittedElementValues.keySet())) {
            throw new InvalidValuesException("submitted elements incoherency");
        }
    }

    private String generateQRCode(String url) {
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
            return "data:image/png;base64,".concat(Base64.getEncoder().encodeToString(out.toByteArray()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to generate QR Code for url", url);
            return null;
        }
    }

    private RenderingLayout buildThemeInfo(String themeKey, String language) throws EntityNotFoundException, ModelDataSerializationException {
        RenderingLayout renderingLayout = new RenderingLayout();
        if (StringUtils.isNotEmpty(themeKey)) {
            ModelVersion themeVersion = ModelVersion.SystemHelper.findActiveVersionByKey(themeKey);
            Theme theme = (Theme) themeVersion.getData(language);
            renderingLayout.setThemeKey(themeKey);
            if (theme.getLogoPath() != null && !theme.getLogoPath().isEmpty()) {
                renderingLayout.setLogoPath(theme.getLogoPath());
                renderingLayout.setLogoAltText(theme.getLogoAltText());
                renderingLayout.setLogoPosition(theme.getLogoPosition().name().toLowerCase());
            }
            renderingLayout.setThemePath(config.publicUrl() + "/models/serials/" + themeVersion.serial + "/data");
        }
        return renderingLayout;
    }

    protected void onStart(@Observes StartupEvent ev) throws IOException, URISyntaxException {
        LOGGER.log(Level.FINE, "Application is starting, importing receipts");
        URL receipts = getClass().getClassLoader().getResource("receipts");
        if (receipts != null) {
            FileSystem fs = null;
            if (receipts.toURI().getScheme().equals("jar")) {
                // FileSystem creation is needed to access files inside a JAR
                fs = FileSystems.newFileSystem(receipts.toURI(), Collections.emptyMap());
            }
            Files.walk(Path.of(receipts.toURI()))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try (InputStream inputStream = Files.newInputStream(path)) {
                            String fileName = path.getFileName().toString();
                            LOGGER.log(Level.INFO, "Importing receipt file: " + fileName);
                            JAXBContext jaxbContext = JAXBContext.newInstance(Receipt.class);
                            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                            this.store.put((Receipt) unmarshaller.unmarshal(inputStream));
                        } catch (IOException | ReceiptStoreException | JAXBException e) {
                            LOGGER.log(Level.SEVERE, "Unable to import receipt: " + e.getMessage(), e);
                        } catch (ReceiptAlreadyExistsException e) {
                            LOGGER.log(Level.INFO, "Receipt already imported");
                        }
                    });
            if (fs != null) {
                fs.close();
            }
        }
    }

    private ModelVersion updateVersionContent(ModelVersion version, Map<String, ModelData> data, String defaultLanguage, String author) throws ConsentManagerException, ModelDataSerializationException {
        if (data.containsKey(defaultLanguage)) {
            version.defaultLanguage = defaultLanguage;
        } else {
            throw new ConsentManagerException("Default language does not exist in content languages");
        }
        long now = System.currentTimeMillis();
        version.availableLanguages = String.join(",", data.keySet());
        version.content.clear();
        for (Map.Entry<String, ModelData> entry : data.entrySet()) {
            version.content.put(entry.getKey(), new ModelContent().withAuthor(author).withDataObject(entry.getValue()));
        }
        version.modificationDate = now;
        version.persist();
        if (previewCache.containsKey(version.entry.id)) {
            previewCache.put(version.entry.id, version);
        }
        version.entry.modificationDate = now;
        version.entry.persist();
        return version;
    }

    private Map<String, List<Record>> listRecordsWithStatus(RecordFilter filter) {
        Stream<Record> records = Record.stream(filter.getQueryString(), filter.getQueryParams());
        Map<String, List<Record>> result = records.collect(Collectors.groupingBy(record -> record.bodyKey));
        result.forEach((key, value) -> recordStatusChain.apply(value));
        return result;
    }
}
