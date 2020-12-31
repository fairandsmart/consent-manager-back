package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 *
 * Authors:
 *
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.api.dto.SubjectDto;
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
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.model.*;
import com.fairandsmart.consent.manager.render.ReceiptRenderer;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import com.fairandsmart.consent.manager.render.RenderingException;
import com.fairandsmart.consent.manager.rule.BasicRecordStatusRuleChain;
import com.fairandsmart.consent.manager.store.LocalReceiptStore;
import com.fairandsmart.consent.manager.store.ReceiptAlreadyExistsException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.manager.store.ReceiptStoreException;
import com.fairandsmart.consent.notification.NotificationService;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import com.fairandsmart.consent.stats.StatisticsStore;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    LocalReceiptStore store;

    @Inject
    NotificationService notification;

    @Inject
    MainConfig config;

    /* Keys are ModelEntries ids since only one version per entry can be previewed. It helps clearing the cache when an entry is deleted. */
    @Inject
    PreviewCache previewCache;

    @Inject
    StatisticsStore statisticsStore;

    @Inject
    BasicRecordStatusRuleChain recordStatusChain;

    @Inject
    Instance<ReceiptRenderer> renderers;

    /* MODELS MANAGEMENT */

    @Override
    public CollectionPage<ModelEntry> listEntries(ModelFilter filter) {
        LOGGER.log(Level.INFO, "Listing models entries");
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
    public ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Creating new entry");
        authentication.ensureConnectedIdentifierIsAdmin();
        if (ModelEntry.isKeyAlreadyExists(key)) {
            throw new EntityAlreadyExistsException("A model entry already exists with key: " + key);
        }
        ModelEntry entry = new ModelEntry();
        entry.type = type;
        entry.key = key;
        entry.name = name;
        entry.description = description;
        entry.branches = DEFAULT_BRANCHE;
        entry.author = authentication.getConnectedIdentifier();
        entry.persist();
        return entry;
    }

    @Override
    public ModelEntry getEntry(String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Getting entry for id: " + id);
        authentication.ensureConnectedIdentifierIsOperator();
        Optional<ModelEntry> optional = ModelEntry.findByIdOptional(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for id: " + id));
    }

    @Override
    public ModelEntry findEntryForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding entry for key: " + key);
        Optional<ModelEntry> optional = ModelEntry.find("key = ?1", key).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for key: " + key));
    }

    @Override
    @Transactional
    public ModelEntry updateEntry(String id, String name, String description) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Updating entry for id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelEntry> optional = ModelEntry.findByIdOptional(id);
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
        List<ModelVersion> versions = ModelVersion.find("entry.id = ?1", id).list();
        if (versions.isEmpty() || versions.stream().allMatch(v -> v.status.equals(ModelVersion.Status.DRAFT))) {
            versions.forEach(v -> ModelVersion.deleteById(v.id));
            ModelEntry.deleteById(id);
            previewCache.remove(id);
        } else {
            throw new ConsentManagerException("unable to delete entry that have not only DRAFT versions");
        }
    }

    @Override
    @Transactional
    public ModelVersion createVersion(String entryId, String defaultLanguage, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Creating new version for entry with id: " + entryId);
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
                LOGGER.log(Level.INFO, "No existing version found, creating first one");
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
                LOGGER.log(Level.INFO, "Latest version found, creating new one");
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
            if (data.containsKey(defaultLanguage)) {
                latest.defaultLanguage = defaultLanguage;
            } else {
                throw new ConsentManagerException("Default language does not exist in content languages");
            }
            latest.availableLanguages = String.join(",", data.keySet());
            latest.content.clear();
            for (Map.Entry<String, ModelData> e : data.entrySet()) {
                latest.content.put(e.getKey(), new ModelContent().withAuthor(connectedIdentifier).withDataObject(e.getValue()));
            }
            latest.modificationDate = now;
            latest.persist();
            if (previewCache.containsKey(latest.entry.id)) {
                previewCache.put(latest.entry.id, latest);
            }
            return latest;
        } catch (SerialGeneratorException | ModelDataSerializationException ex) {
            throw new ConsentManagerException("unable to create new version", ex);
        }
    }

    @Override
    public ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding active version for entry with key: " + key);
        return ModelVersion.SystemHelper.findActiveVersionByKey(key);
    }

    @Override
    public ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding active version for entry with id: " + entryId);
        return ModelVersion.SystemHelper.findActiveVersionByEntryId(entryId);
    }

    @Override
    public ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding latest version for entry with key: " + key);
        Optional<ModelVersion> voptional = ModelVersion.find("entry.key = ?1 and child = ?2", key, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with key: " + key));
    }

    @Override
    public ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding latest version for entry with id: " + entryId);
        Optional<ModelVersion> voptional = ModelVersion.find("entry.id = ?1 and child = ?2", entryId, "").singleResultOptional();
        return voptional.orElseThrow(() -> new EntityNotFoundException("unable to find latest version for entry with id: " + entryId));
    }

    @Override
    public ModelVersion getVersion(String id) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding version for id: " + id);
        Optional<ModelVersion> optional = ModelVersion.findByIdOptional(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for id: " + id));
    }

    @Override
    public ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "Finding version for serial: " + serial);
        Optional<ModelVersion> optional = ModelVersion.find("serial = ?1", serial).singleResultOptional();
        return optional.orElseThrow(() -> new EntityNotFoundException("unable to find a version for serial: " + serial));
    }

    @Override
    public List<ModelVersion> getVersionHistoryForKey(String key) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Listing versions for entry with key: " + key);
        List<ModelVersion> versions = ModelVersion.find("entry.key = ?1", key).list();
        if (!versions.isEmpty()) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    public List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "Listing versions for entry with id: " + entryId);
        List<ModelVersion> versions = ModelVersion.find("entry.id = ?1", entryId).list();
        if (!versions.isEmpty()) {
            return ModelVersion.HistoryHelper.orderVersions(versions);
        }
        return versions;
    }

    @Override
    @Transactional
    public ModelVersion updateVersion(String id, String defaultLanguage, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating content for version with id: " + id);
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
        try {
            if (data.containsKey(defaultLanguage)) {
                version.defaultLanguage = defaultLanguage;
            } else {
                throw new ConsentManagerException("Default language does not exist in content languages");
            }
            version.availableLanguages = String.join(",", data.keySet());
            version.content.clear();
            for (Map.Entry<String, ModelData> entry : data.entrySet()) {
                version.content.put(entry.getKey(), new ModelContent().withAuthor(connectedIdentifier).withDataObject(entry.getValue()));
            }
            version.modificationDate = System.currentTimeMillis();
            version.persist();
            if (previewCache.containsKey(version.entry.id)) {
                previewCache.put(version.entry.id, version);
            }
            return version;
        } catch (ModelDataSerializationException ex) {
            throw new ConsentManagerException("Unable to serialise data", ex);
        }

    }

    @Override
    @Transactional
    public ModelVersion updateVersionType(String id, ModelVersion.Type type) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating type for version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelVersion> voptional = ModelVersion.findByIdOptional(id);
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
        } else if (status.equals(ModelVersion.Status.ARCHIVED)) {
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
        LOGGER.log(Level.INFO, "Deleting version with id: " + id);
        authentication.ensureConnectedIdentifierIsAdmin();
        Optional<ModelVersion> voptional = ModelVersion.findByIdOptional(id);
        ModelVersion version = voptional.orElseThrow(() -> new EntityNotFoundException("unable to find a version with id: " + id));
        if (!version.child.isEmpty()) {
            throw new ConsentManagerException("Unable to delete version that is not last one");
        }
        if (!version.status.equals(ModelVersion.Status.DRAFT)) {
            throw new ConsentManagerException("Unable to delete version that is not DRAFT");
        }
        ModelVersion parent = ModelVersion.findById(version.parent);
        parent.child = "";
        version.delete();
        parent.persist();
    }

    /* CONSENT MANAGEMENT */

    @Override
    public String buildToken(ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Building generate form token for context: " + ctx);
        if (ctx.getSubject() == null || ctx.getSubject().isEmpty()) {
            ctx.setSubject(authentication.getConnectedIdentifier());
        } else if (!ctx.getSubject().equals(authentication.getConnectedIdentifier()) && !authentication.isConnectedIdentifierApi()) {
            throw new AccessDeniedException("Only admin, operator or api can generate token for other identifier than connected one");
        }
        return tokenService.generateToken(ctx);
    }

    @Override
    public ConsentForm generateForm(String token) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException {
        LOGGER.log(Level.INFO, "Generating consent form");
        try {
            ConsentContext ctx = (ConsentContext) this.tokenService.readToken(token);

            // Initialize form
            ConsentForm form = new ConsentForm();
            form.setLanguage(ctx.getLanguage());
            form.setOrientation(ctx.getOrientation());
            form.setPreview(ctx.isPreview());
            form.setShowAcceptAll(ctx.isShowAcceptAll());
            form.setAcceptAllText(ctx.getAcceptAllText());
            form.setFooterOnTop(ctx.isFooterOnTop());

            // Fetch elements from context
            List<String> elementsKeys = new ArrayList<>();
            List<ModelVersion> elementsVersions = new ArrayList<>();
            Map<String, String> elementsDependencies = new HashMap<>();
            for (String element : ctx.getElements()) {
                String key = (ConsentElementIdentifier.isValid(element)) ? ConsentElementIdentifier.deserialize(element).getKey() : element;
                elementsKeys.add(key);
                elementsVersions.add(ModelVersion.SystemHelper.findActiveVersionByKey(key));
            }

            // Fetch preferences associated to processing
            if (ctx.isAssociatePreferences()) {
                int processingIndex = elementsVersions.size() - 1;
                while (processingIndex >= 0) {
                    if (Processing.TYPE.equals(elementsVersions.get(processingIndex).entry.type)) {
                        Processing processing = ((Processing) elementsVersions.get(processingIndex).content.get(ctx.getLanguage()).getDataObject());
                        List<String> preferences = processing.getAssociatedPreferences().stream().filter(key -> !elementsKeys.contains(key)).collect(Collectors.toList());
                        for (String key : preferences) {
                            elementsKeys.add(processingIndex + 1, key);
                            ModelVersion version = ModelVersion.SystemHelper.findActiveVersionByKey(key);
                            elementsDependencies.put(version.serial, elementsVersions.get(processingIndex).getIdentifier().toString());
                            elementsVersions.add(processingIndex + 1, version);
                        }
                    }
                    processingIndex--;
                }
            }

            form.setElementsDependencies(elementsDependencies);

            // Fetch previous records
            Map<String, Record> previousRecords = new HashMap<>();
            if (!ctx.isPreview()) {
                ctx.setElements(elementsKeys);
                previousRecords = systemListContextValidRecords(ctx);
            }

            // Update form with previous values and update form & context with elements
            List<String> elementsIdentifiers = new ArrayList<>();
            for (int elementIndex = 0; elementIndex < elementsKeys.size(); elementIndex++) {
                String key = elementsKeys.get(elementIndex);
                ModelVersion version = elementsVersions.get(elementIndex);
                if (previousRecords.containsKey(key)) {
                    form.addPreviousValue(version.serial, previousRecords.get(key).value);
                }
                if (ctx.getFormType().equals(ConsentContext.FormType.FULL) || !form.getPreviousValues().containsKey(version.serial)) {
                    form.addElement(version);
                    elementsIdentifiers.add(version.getIdentifier().serialize());
                }
            }
            ctx.setElements(elementsIdentifiers);

            // Update form & context with basic infos
            if (!StringUtils.isEmpty(ctx.getInfo())) {
                String key = (ConsentElementIdentifier.isValid(ctx.getInfo())) ? ConsentElementIdentifier.deserialize(ctx.getInfo()).getKey() : ctx.getInfo();
                ModelVersion info = ModelVersion.SystemHelper.findActiveVersionByKey(key);
                form.setInfo(info);
                ctx.setInfo(info.getIdentifier().serialize());
            }

            // Update form & context with theme
            if (!StringUtils.isEmpty(ctx.getTheme())) {
                String key = (ConsentElementIdentifier.isValid(ctx.getTheme())) ? ConsentElementIdentifier.deserialize(ctx.getTheme()).getKey() : ctx.getTheme();
                ModelVersion theme = ModelVersion.SystemHelper.findActiveVersionByKey(key);
                form.setTheme(theme);
                ctx.setTheme(theme.getIdentifier().serialize());
            }

            // Update form & context with notification model
            if (!StringUtils.isEmpty(ctx.getNotificationModel())) {
                String key = (ConsentElementIdentifier.isValid(ctx.getNotificationModel())) ? ConsentElementIdentifier.deserialize(ctx.getNotificationModel()).getKey() : ctx.getNotificationModel();
                ModelVersion notification = ModelVersion.SystemHelper.findActiveVersionByKey(key);
                ctx.setNotificationModel(notification.getIdentifier().serialize());
            }

            // Update form token
            form.setToken(this.tokenService.generateToken(ctx));
            return form;
        } catch (TokenServiceException | IllegalIdentifierException | ModelDataSerializationException e) {
            throw new ConsentServiceException("Unable to generate consent form", e);
        }
    }

    @Override
    @Transactional
    public ConsentTransaction submitConsent(String token, MultivaluedMap<String, String> values) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, InvalidConsentException {
        LOGGER.log(Level.INFO, "Submitting consent");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        try {
            ConsentContext ctx = (ConsentContext) this.tokenService.readToken(token);
            if (StringUtils.isEmpty(ctx.getSubject())) {
                throw new ConsentServiceException("Subject is empty");
            }

            Map<String, String> valuesMap = new HashMap<>();
            for (MultivaluedMap.Entry<String, List<String>> value : values.entrySet()) {
                valuesMap.put(value.getKey(), String.join(",", value.getValue()));
            }
            //TODO Check that identifiers refers to the latest elements versions to avoid committing a consent on a staled version of an entry
            // (aka form is generated before a MAJOR RELEASE and submit after)
            // Maybe use a global referential hash that would be injected in token and which could be stored as a whole database integrity check
            // Any change in a entry would modify this hash and avoid checking each element but only a in memory or in database single value
            String receiptId = this.saveConsent(ctx, valuesMap);
            ctx.setReceiptId(receiptId);

            Event<ConsentContext> event = new Event<ConsentContext>().withAuthor(connectedIdentifier).withType(Event.CONSENT_SUBMIT).withData(ctx);
            this.notification.notify(event);

            return new ConsentTransaction(receiptId);
        } catch (TokenServiceException | ConsentServiceException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    /* SUBJECTS */

    @Override
    public List<Subject> findSubjects(String name) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Searching subjects");
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to search for subjects");
        }
        return Subject.list("name like ?1", Sort.by("name", Sort.Direction.Descending), "%" + name + "%");
    }

    @Override
    public Subject getSubject(String name) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Getting subject for name: " + name);
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
    public Subject createSubject(SubjectDto subjectDto) throws ConsentManagerException, EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "Creating subject with name: " + subjectDto.getName());
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to create subjects");
        }
        if (StringUtils.isEmpty(subjectDto.getName())) {
            throw new ConsentManagerException("Subject name cannot be empty");
        }
        Optional<Subject> optional = Subject.find("name = ?1", subjectDto.getName()).singleResultOptional();
        if (optional.isPresent()) {
            throw new EntityAlreadyExistsException("This subject name already exists");
        }
        Instant now = Instant.now();
        Subject subject = new Subject();
        subject.name = subjectDto.getName();
        subject.creationTimestamp = now.toEpochMilli();
        subject.emailAddress = subjectDto.getEmailAddress();
        subject.persist();
        statisticsStore.add("subjects", "subjects");
        return subject;
    }

    @Override
    @Transactional
    public Subject updateSubject(String subjectId, SubjectDto subjectDto) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Updating subject with id: " + subjectId);
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to update subjects");
        }
        Optional<Subject> optional = Subject.findByIdOptional(subjectId);
        Subject subject = optional.orElseThrow(() -> new EntityNotFoundException("Unable to find a subject for id: " + subjectId));
        if (!subject.name.equals(subjectDto.getName())) {
            throw new ConsentManagerException("Subject name cannot be changed");
        }
        subject.emailAddress = subjectDto.getEmailAddress();
        subject.persist();
        return subject;
    }

    /* RECORDS */

    @Override
    public Map<String, List<Record>> listSubjectRecords(String subject) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Listing records for subject");
        if (!authentication.isConnectedIdentifierOperator() && !subject.equals(authentication.getConnectedIdentifier())) {
            throw new AccessDeniedException("You must be operator to perform records search");
        }
        RecordFilter filter = new RecordFilter();
        filter.setState(Record.State.COMMITTED);
        filter.setSubject(subject);
        Stream<Record> records = Record.stream(filter.getQueryString(), filter.getQueryParams());
        Map<String, List<Record>> result = records.collect(Collectors.groupingBy(record -> record.bodyKey));
        result.forEach((key, value) -> recordStatusChain.apply(value));
        return result;
        //TODO We could create a subject based record cache avoiding checking all of that each request
        //  cache will be invalidated :
        //  - when a consent is submitted for the subject
        //  - when a model is modified
        //  Maybe it should be included in a PRO version
    }

    @Override
    public Map<String, Record> systemListContextValidRecords(ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Listing records");
        RecordFilter filter = new RecordFilter();
        filter.setSubject(ctx.getSubject());
        filter.setState(Record.State.COMMITTED);
        if (ctx.getInfo() != null && !ctx.getInfo().isEmpty()) {
            filter.setInfos(ModelVersion.SystemHelper.findActiveSerialsForKey(ctx.getInfo()));
        }
        filter.setElements(ctx.getElements().stream().flatMap(e -> ModelVersion.SystemHelper.findActiveSerialsForKey(e).stream()).collect(Collectors.toList()));
        Stream<Record> allRecords = Record.stream(filter.getQueryString(), filter.getQueryParams());
        Map<String, List<Record>> result = allRecords.collect(Collectors.groupingBy(record -> record.bodyKey));
        result.forEach((key, value) -> recordStatusChain.apply(value));
        return result.entrySet().stream().filter(entry -> entry.getValue().stream().anyMatch(record -> record.status.equals(Record.Status.VALID)))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().filter(record -> record.status.equals(Record.Status.VALID)).findFirst().get()));
    }

    @Override
    public Map<Subject, Record> extractRecords(String key, String value, boolean regexpValue) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Extracting valid records with key: " + key + " and value: " + value + ((regexpValue)?"(regexp)":"(exact match)"));
        if (!authentication.isConnectedIdentifierAdmin()) {
            throw new AccessDeniedException("You must be admin to search subjects");
        }
        Map<Subject, Record> result = new HashMap<>();
        List<String> serials = ModelVersion.SystemHelper.findActiveSerialsForKey(key);
        List<Subject> subjects = Subject.listAll();
        final Pattern pattern = regexpValue ? Pattern.compile(value) : null;
        subjects.forEach(subject -> {
            RecordFilter filter = new RecordFilter();
            filter.setSubject(subject.name);
            filter.setState(Record.State.COMMITTED);
            filter.setElements(serials);
            List<Record> records = Record.list(filter.getQueryString(), filter.getQueryParams());
            recordStatusChain.apply(records);
            LOGGER.log(Level.FINE, "Rules applied on loaded records of subject : " + subject + ": " + records);
            records.stream()
                    .filter(r -> r.status.equals(Record.Status.VALID)
                            && (pattern != null ? pattern.matcher(r.value).matches() : r.value.equals(value)))
                    .findFirst()
                    .ifPresent(r -> result.put(subject, r));
        });
        return result;
    }

    /* RECEIPTS */

    @Override
    public Receipt getReceipt(String token, String id) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException {
        LOGGER.log(Level.INFO, "Getting receipt for id: " + id);
        try {
            String xml = IOUtils.toString(store.get(id), StandardCharsets.UTF_8.name());
            Receipt receipt = Receipt.build(xml);
            if (!authentication.getConnectedIdentifier().equals(receipt.getSubject()) && !authentication.isConnectedIdentifierOperator()) {
                if (StringUtils.isNotEmpty(token)) {
                    ConsentTransaction tx = (ConsentTransaction) tokenService.readToken(token);
                    if (!tx.getTransaction().equals(receipt.getTransaction())) {
                        throw new AccessDeniedException("Token transaction is not the same than receipt");
                    }
                } else {
                    throw new AccessDeniedException("You must be operator to retrieve receipts of other subjects");
                }
            }
            return receipt;
        } catch (IOException | ReceiptStoreException | JAXBException e) {
            throw new ConsentManagerException("Unable to read receipt from store", e);
        }
    }

    @Override
    public byte[] renderReceipt(String token, String id, String format) throws ReceiptNotFoundException, ConsentManagerException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException {
        LOGGER.log(Level.INFO, "Rendering receipt for id: " + id + " and format: " + format);
        Receipt receipt = getReceipt(token, id);
        Optional<ReceiptRenderer> renderer = renderers.stream().filter(r -> r.format().equals(format)).findFirst();
        if (renderer.isPresent()) {
            return renderer.get().render(receipt);
        }
        throw new ReceiptRendererNotFoundException("unable to find a receipt renderer for format: " + format);
    }

    @Override
    public byte[] systemRenderReceipt(String id, String format) throws ReceiptRendererNotFoundException, ReceiptStoreException, ReceiptNotFoundException, RenderingException {
        LOGGER.log(Level.INFO, "##SYSTEM## Rendering receipt for id: " + id + " and format: " + format);
        try {
            Receipt receipt = Receipt.build(IOUtils.toString(store.get(id), StandardCharsets.UTF_8.name()));
            Optional<ReceiptRenderer> renderer = renderers.stream().filter(r -> r.format().equals(format)).findFirst();
            if (renderer.isPresent()) {
                return renderer.get().render(receipt);

            }
        } catch (IOException | JAXBException e) {
            throw new RenderingException("unable to render receipt", e);
        }
        throw new ReceiptRendererNotFoundException("unable to find a receipt renderer for format: " + format);
    }

    /* INTERNAL */

    private void checkValuesCoherency(ConsentContext ctx, Map<String, String> values) throws InvalidConsentException {
        if (ctx.getInfo() == null && values.containsKey("info")) {
            throw new InvalidConsentException("submitted basic info incoherency, expected: null got: " + values.get("info"));
        }
        if (!StringUtils.isEmpty(ctx.getInfo()) && (!values.containsKey("info") || !values.get("info").equals(ctx.getInfo()))) {
            throw new InvalidConsentException("submitted basic info incoherency, expected: " + ctx.getInfo() + " got: " + values.get("info"));
        }
        Map<String, String> submittedElementValues = values.entrySet().stream()
                .filter(e -> e.getKey().startsWith("element") && !e.getKey().endsWith("-optional"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Remove ignored optional preferences from context
        List<String> ignoredElements = values.keySet().stream()
                .filter(key -> key.endsWith("-optional")).map(key -> key.replace("-optional", ""))
                .filter(key -> !submittedElementValues.containsKey(key)).collect(Collectors.toList());
        ctx.setElements(ctx.getElements().stream().filter(e -> !ignoredElements.contains(e)).collect(Collectors.toList()));
        values.keySet().removeIf(key -> key.endsWith("-optional"));

        if (!new HashSet<>(ctx.getElements()).equals(submittedElementValues.keySet())) {
            throw new InvalidConsentException("submitted elements incoherency");
        }
    }

    private String saveConsent(ConsentContext ctx, Map<String, String> values) throws ConsentServiceException, InvalidConsentException {
        try {
            this.checkValuesCoherency(ctx, values);
            String transaction = Base58.encodeUUID(UUID.randomUUID().toString());
            Instant now = Instant.now();

            ConsentElementIdentifier infoId = null;
            if (!StringUtils.isEmpty(ctx.getInfo())) {
                infoId = ConsentElementIdentifier.deserialize(ctx.getInfo());
            }

            if (!Subject.exists(ctx.getSubject())) {
                Subject subject = new Subject();
                subject.name = ctx.getSubject();
                subject.creationTimestamp = now.toEpochMilli();
                Subject.persist(subject);
                statisticsStore.add("subjects", "subjects");
            }

            String comment = "";
            if (values.containsKey("comment")) {
                comment = values.get("comment");
            }

            List<Record> records = new ArrayList<>();
            List<String> recordsKeys = new ArrayList<>();
            for (Map.Entry<String, String> value : values.entrySet()) {
                try {
                    ConsentElementIdentifier bodyId = ConsentElementIdentifier.deserialize(value.getKey());
                    recordsKeys.add(bodyId.getKey());
                    Record record = new Record();
                    record.transaction = transaction;
                    record.subject = ctx.getSubject();
                    record.type = bodyId.getType();
                    record.infoSerial = infoId != null ? infoId.getSerial() : "";
                    record.bodySerial = bodyId.getSerial();
                    record.infoKey = infoId != null ? infoId.getKey() : "";
                    record.bodyKey = bodyId.getKey();
                    record.serial = (record.infoSerial.isEmpty() ? "" : record.infoSerial + ".") + record.bodySerial;
                    record.value = value.getValue();
                    record.creationTimestamp = now.toEpochMilli();
                    record.expirationTimestamp = Conditions.TYPE.equals(record.type) ? 0 : now.plusMillis(ctx.getValidityInMillis()).toEpochMilli();
                    record.state = Record.State.COMMITTED;
                    record.collectionMethod = ctx.getCollectionMethod();
                    record.author = !StringUtils.isEmpty(ctx.getAuthor()) ? ctx.getAuthor() : authentication.getConnectedIdentifier();
                    record.comment = comment;
                    record.persist();
                    statisticsStore.add("records", record.type);
                    records.add(record);
                } catch (IllegalIdentifierException e) {
                    //
                }
            }
            ctx.setElements(recordsKeys);
            LOGGER.log(Level.FINE, "records: " + records);

            Receipt receipt;
            if (ctx.getReceiptDeliveryType().equals(ConsentContext.ReceiptDeliveryType.NONE)) {
                receipt = new Receipt();
                receipt.setLanguage(ctx.getLanguage());
            } else {
                BasicInfo info = null;
                if (infoId != null) {
                    info = (BasicInfo) ModelVersion.SystemHelper.findModelVersionForSerial(infoId.getSerial(), false).getData(ctx.getLanguage());
                    ctx.setInfo(infoId.getKey());
                }
                Map<Processing, Record> trecords = new HashMap<>();
                records.stream().filter(r -> r.type.equals(Processing.TYPE)).forEach(r -> {
                    try {
                        Processing t = (Processing) ModelVersion.SystemHelper.findModelVersionForSerial(r.bodySerial, false).getData(ctx.getLanguage());
                        trecords.put(t, r);
                    } catch (EntityNotFoundException | ModelDataSerializationException e) {
                        //
                    }
                });
                receipt = Receipt.build(transaction, config.processor(), ZonedDateTime.ofInstant(now, ZoneId.of("UTC")), ctx, info, trecords);
                if (ctx.getNotificationRecipient() != null && !ctx.getNotificationRecipient().isEmpty()) {
                    receipt.setNotificationType("email");
                    receipt.setNotificationRecipient(ctx.getNotificationRecipient());
                }

                ctx.setCollectionMethod(ConsentContext.CollectionMethod.RECEIPT);
                String token = tokenService.generateToken(ctx, Date.from(receipt.getExpirationDate().toInstant()));
                receipt.setUpdateUrl(config.publicUrl() + "/consents?t=" + token);
                try {
                    BitMatrix bitMatrix = new QRCodeWriter().encode(receipt.getUpdateUrl(), BarcodeFormat.QR_CODE, 300, 300);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                    receipt.setUpdateUrlQrCode("data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray()));
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Unable to generate QRCode for receipt", e);
                }

                if (ctx.getTheme() != null && ctx.getTheme().length() > 1) {
                    receipt.setTheme(ctx.getTheme());
                    ConsentElementIdentifier themeId = ConsentElementIdentifier.deserialize(ctx.getTheme());
                    Theme theme = (Theme) ModelVersion.SystemHelper.findModelVersionForSerial(themeId.getSerial(), false).getData(ctx.getLanguage());
                    if (theme.getLogoPath() != null && !theme.getLogoPath().isEmpty()) {
                        receipt.setLogoPath(theme.getLogoPath());
                        receipt.setLogoAltText(theme.getLogoAltText());
                        receipt.setLogoPosition(theme.getLogoPosition().name());
                    }
                    receipt.setThemePath(config.publicUrl() + "/models/serials/" + themeId.getSerial() + "/data");
                }

                LOGGER.log(Level.INFO, "Receipt XML: " + receipt.toXml());
                //TODO Sign the receipt...
                byte[] xml = receipt.toXmlBytes();
                store.put(receipt.getTransaction(), xml);
            }
            return receipt.getTransaction();
        } catch (EntityNotFoundException | ModelDataSerializationException | JAXBException | ReceiptAlreadyExistsException | ReceiptStoreException | IllegalIdentifierException | DatatypeConfigurationException e) {
            throw new ConsentServiceException("Unable to submit consent", e);
        }
    }

    protected void onStart(@Observes StartupEvent ev) throws IOException, URISyntaxException {
        LOGGER.log(Level.INFO, "Application is starting, importing receipts");
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
                            LOGGER.log(Level.INFO, "Importing receipt " + fileName);
                            this.store.put(fileName, inputStream);
                        } catch (IOException | ReceiptStoreException e) {
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

}
