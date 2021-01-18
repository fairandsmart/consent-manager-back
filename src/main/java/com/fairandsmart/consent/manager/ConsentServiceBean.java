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
import com.fairandsmart.consent.manager.exception.ConsentServiceException;
import com.fairandsmart.consent.manager.exception.InvalidConsentException;
import com.fairandsmart.consent.manager.exception.InvalidStatusException;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.model.BasicInfo;
import com.fairandsmart.consent.manager.model.Processing;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.manager.model.Theme;
import com.fairandsmart.consent.manager.render.*;
import com.fairandsmart.consent.manager.rule.BasicRecordStatusRuleChain;
import com.fairandsmart.consent.manager.store.LocalFolderReceiptStore;
import com.fairandsmart.consent.manager.store.ReceiptAlreadyExistsException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.manager.store.ReceiptStoreException;
import com.fairandsmart.consent.notification.NotificationService;
import com.fairandsmart.consent.notification.entity.Event;
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
            return this.updateVersionContent(latest, data, defaultLanguage, connectedIdentifier);
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
        if (!version.status.equals(ModelVersion.Status.DRAFT)) {
            throw new ConsentManagerException("Unable to update type for version that is not DRAFT");
        }
        try {
            return this.updateVersionContent(version, data, defaultLanguage, connectedIdentifier);
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
        Optional<ModelVersion> optionalParent = ModelVersion.findByIdOptional(version.parent);
        if (optionalParent.isPresent()) {
            ModelVersion parent = optionalParent.get();
            parent.child = "";
            parent.persist();
        }
        version.delete();
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

            //Initialise form using context
            ConsentForm form = new ConsentForm(ctx);

            // Fetch elements from context
            List<String> elementsKeys = this.extractElementsKeys(ctx.getElements());
            List<ModelVersion> elementsVersions = ModelVersion.SystemHelper.findActiveVersionsForKeys(elementsKeys);

            // Fetch previous records
            if (!ctx.isPreview()) {
                final Map<String, Record> previousRecords = systemListValidRecords(ctx.getSubject(), ctx.getInfo(), elementsKeys);
                form.setPreviousValues(elementsVersions.stream().filter(version -> previousRecords.containsKey(version.entry.key)).collect(Collectors.toMap((v) -> v.serial, (v) -> previousRecords.get(v.entry.key).value)));
            }

            // Update form and context elements, infos, theme and notification
            form.setElements(elementsVersions.stream().filter(version -> (ctx.getFormType().equals(ConsentContext.FormType.FULL) || !form.getPreviousValues().containsKey(version.serial))).collect(Collectors.toList()));
            ctx.setElements(form.getElements().stream().map(version -> version.getIdentifier().serialize()).collect(Collectors.toList()));
            if (StringUtils.isNotEmpty(ctx.getInfo())) {
                form.setInfo(ModelVersion.SystemHelper.findActiveVersionByKey(extractElementKey(ctx.getInfo())));
                ctx.setInfo(form.getInfo().getIdentifier().serialize());
            }
            if (StringUtils.isNotEmpty(ctx.getTheme())) {
                form.setTheme(ModelVersion.SystemHelper.findActiveVersionByKey(extractElementKey(ctx.getTheme())));
                ctx.setTheme(form.getTheme().getIdentifier().serialize());
            }
            if (StringUtils.isNotEmpty(ctx.getNotificationModel())) {
                ModelVersion notification = ModelVersion.SystemHelper.findActiveVersionByKey(extractElementKey(ctx.getNotificationModel()));
                ctx.setNotificationModel(notification.getIdentifier().serialize());
            }

            form.setToken(this.tokenService.generateToken(ctx));
            return form;
        } catch (TokenServiceException e) {
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

            this.checkValuesCoherency(ctx, valuesMap);
            String transaction = Base58.encodeUUID(UUID.randomUUID().toString());
            Instant now = Instant.now();

            Optional<ConsentElementIdentifier> infoIdOpt = ConsentElementIdentifier.deserialize(ctx.getInfo());
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
            ctx.setInfo(infoId.getKey());

            if (!Subject.exists(ctx.getSubject())) {
                Subject.create(ctx.getSubject()).persist();
            }
            String comment = values.containsKey("comment") ? valuesMap.get("comment") : "";

            List<Record> records = ctx.getElements().stream().map(ConsentElementIdentifier::deserialize).filter(Optional::isPresent).map(
                    opt -> Record.build(ctx, transaction, authentication.getConnectedIdentifier(), now, infoId, opt.get(), valuesMap.get(opt.get().serialize()), comment)
            ).collect(Collectors.toList());
            records.forEach(record -> record.persist());

            List<Pair<Processing, Record>> trecords = new ArrayList<>();
            records.stream().filter(record -> record.type.equals(Processing.TYPE)).forEach(record -> {
                try {
                    Processing processing = (Processing) ModelVersion.SystemHelper.findModelVersionForSerial(record.bodySerial, false).getData(ctx.getLanguage());
                    trecords.add(new ImmutablePair<>(processing, record));
                } catch (EntityNotFoundException | ModelDataSerializationException e) { //
                }
            });
            Receipt receipt = Receipt.build(transaction, config.processor(), ZonedDateTime.ofInstant(now, ZoneId.of("UTC")), ctx, info, trecords);
            ctx.setCollectionMethod(ConsentContext.CollectionMethod.RECEIPT);
            String updateToken = tokenService.generateToken(ctx, Date.from(receipt.getExpirationDate().toInstant()));
            receipt.setUpdateUrl(config.publicUrl() + "/consents?t=" + updateToken);
            receipt.setUpdateUrlQrCode(generateQRCode(receipt.getUpdateUrl()));
            store.put(receipt);

            String receiptId = receipt.getTransaction();
            ctx.setReceiptId(receiptId);

            NotificationReport report;
            if (StringUtils.isNotEmpty(ctx.getNotificationRecipient()) && StringUtils.isNotEmpty(ctx.getNotificationModel())) {
                report = new NotificationReport(ctx.getReceiptId(), NotificationReport.Type.EMAIL, NotificationReport.Status.PENDING);
            } else {
                report = new NotificationReport(ctx.getReceiptId(), NotificationReport.Type.NONE, NotificationReport.Status.NONE);
            }
            this.notification.pushReport(report);

            Event<ConsentContext> event = new Event<ConsentContext>().withAuthor(connectedIdentifier).withType(Event.CONSENT_SUBMIT).withData(ctx);
            this.notification.notify(event);

            return new ConsentTransaction(receiptId);
        } catch (TokenServiceException | ConsentServiceException | EntityNotFoundException | DatatypeConfigurationException | ReceiptStoreException | ReceiptAlreadyExistsException | ModelDataSerializationException e) {
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
    public Subject createSubject(String name, String email) throws ConsentManagerException, EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "Creating subject with name: " + name);
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
        return subject;
    }

    @Override
    @Transactional
    public Subject updateSubject(String id, String email) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "Updating subject with id: " + id);
        if (!authentication.isConnectedIdentifierOperator()) {
            throw new AccessDeniedException("You must be operator to update subjects");
        }
        Optional<Subject> optional = Subject.findByIdOptional(id);
        Subject subject = optional.orElseThrow(() -> new EntityNotFoundException("Unable to find a subject for id: " + id));
        subject.emailAddress = email;
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
        return this.listRecordsWithStatus(filter);
    }

    @Override
    public Map<String, Record> systemListValidRecords(String subject, String infoKey, List<String> elementsKeys) {
        LOGGER.log(Level.INFO, "Listing valid records");
        RecordFilter filter = new RecordFilter();
        filter.setSubject(subject);
        filter.setState(Record.State.COMMITTED);
        if (infoKey != null && !infoKey.isEmpty()) {
            filter.setInfos(ModelVersion.SystemHelper.findActiveSerialsForKey(infoKey));
        }
        filter.setElements(elementsKeys.stream().flatMap(e -> ModelVersion.SystemHelper.findActiveSerialsForKey(e).stream()).collect(Collectors.toList()));
        Map<String, List<Record>> result = this.listRecordsWithStatus(filter);
        return result.entrySet().stream().filter(entry -> entry.getValue().stream().anyMatch(record -> record.status.equals(Record.Status.VALID)))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().filter(record -> record.status.equals(Record.Status.VALID)).findFirst().get()));
    }

    @Override
    public Map<Subject, Record> extractRecords(String key, String value, boolean regexpValue) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Extracting valid records with key: " + key + " and value: " + value + ((regexpValue) ? "(regexp)" : "(exact match)"));
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
            return receipt;
        } catch (ReceiptStoreException e) {
            throw new ConsentManagerException("Unable to read receipt from store", e);
        }
    }

    @Override
    public byte[] renderReceipt(String token, String id, String format, String themeKey) throws ReceiptNotFoundException, ConsentManagerException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "Rendering receipt for id: " + id + " and format: " + format + " and theme: " + themeKey);
        Receipt receipt = getReceipt(token, id);
        return this.internalRenderReceipt(receipt, format, themeKey);
    }

    @Override
    public byte[] systemRenderReceipt(String id, String format, String themeKey) throws ReceiptRendererNotFoundException, ReceiptStoreException, ReceiptNotFoundException, RenderingException, ModelDataSerializationException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "##SYSTEM## Rendering receipt for id: " + id + " and format: " + format + " and theme: " + themeKey);
        Receipt receipt = store.get(id);
        return this.internalRenderReceipt(receipt, format, themeKey);
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

    private void checkValuesCoherency(ConsentContext ctx, Map<String, String> values) throws InvalidConsentException {
        if (ctx.getInfo() == null && values.containsKey("info")) {
            throw new InvalidConsentException("submitted basic info incoherency, expected: null got: " + values.get("info"));
        }
        if (StringUtils.isNotEmpty(ctx.getInfo()) && (!values.containsKey("info") || !values.get("info").equals(ctx.getInfo()))) {
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
        version.availableLanguages = String.join(",", data.keySet());
        version.content.clear();
        for (Map.Entry<String, ModelData> entry : data.entrySet()) {
            version.content.put(entry.getKey(), new ModelContent().withAuthor(author).withDataObject(entry.getValue()));
        }
        version.modificationDate = System.currentTimeMillis();
        version.persist();
        if (previewCache.containsKey(version.entry.id)) {
            previewCache.put(version.entry.id, version);
        }
        return version;
    }

    private Map<String, List<Record>> listRecordsWithStatus(RecordFilter filter) {
        Stream<Record> records = Record.stream(filter.getQueryString(), filter.getQueryParams());
        Map<String, List<Record>> result = records.collect(Collectors.groupingBy(record -> record.bodyKey));
        result.forEach((key, value) -> recordStatusChain.apply(value));
        return result;
    }
}
