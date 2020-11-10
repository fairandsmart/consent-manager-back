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

import com.fairandsmart.consent.TestUtils;
import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.SubjectDto;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.model.*;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ConsentServiceTest {

    private static final Logger LOGGER = Logger.getLogger(ConsentServiceTest.class.getName());

    @Inject
    ConsentService service;

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testModelEntryLifecycle() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException, InvalidStatusException {
        LOGGER.info("#### Test BasicInfo entry lifecycle");
        String key = UUID.randomUUID().toString();
        String language = "fr";

        // CREATE
        LOGGER.info("List existing entries for basic infos");
        CollectionPage<ModelEntry> basicInfos = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(BasicInfo.TYPE)).withPage(1).withSize(5));
        long basicInfosCountBeforeCreate = basicInfos.getTotalCount();

        LOGGER.info("Create BasicInfo entry with key " + key);
        ModelEntry entry = service.createEntry(key, "Entry First Name", "Entry First Description", BasicInfo.TYPE);
        assertNotNull(entry);

        LOGGER.info("List existing entries for basic infos");
        basicInfos = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(BasicInfo.TYPE)).withPage(1).withSize(5));
        assertEquals(basicInfosCountBeforeCreate + 1, basicInfos.getTotalCount());

        LOGGER.info("Check creating a new entry with the same key is forbidden");
        assertThrows(EntityAlreadyExistsException.class, () -> service.createEntry(key, "Other Entry Name", "Other Entry Description", BasicInfo.TYPE));

        // READ
        LOGGER.info("Lookup entry with key " + key);
        entry = service.findEntryForKey(key);
        LOGGER.info("Entry: " + entry);
        assertNotNull(entry.id);
        assertNotNull(entry.owner);
        assertEquals(BasicInfo.TYPE, entry.type);
        assertEquals(key, entry.key);
        assertEquals("Entry First Name", entry.name);
        assertEquals("Entry First Description", entry.description);

        // UPDATE
        LOGGER.info("Update entry with key " + key);
        service.updateEntry(entry.id, "Entry Name Updated", "Entry Description Updated");
        entry = service.findEntryForKey(key);
        LOGGER.info("Entry: " + entry);
        assertEquals("Entry Name Updated", entry.name);
        assertEquals("Entry Description Updated", entry.description);

        LOGGER.info("Check updating a entry with a fake key fails");
        String fakeKey = UUID.randomUUID().toString();
        assertThrows(EntityNotFoundException.class, () -> service.updateEntry(fakeKey, "Entry Name Updated", "Entry Description Updated"));

        // DELETE : NO VERSION
        LOGGER.info("Check no version exists");
        List<ModelVersion> versions = service.getVersionHistoryForKey(key);
        assertEquals(0, versions.size());

        LOGGER.info("Delete entry with key " + key);
        service.deleteEntry(entry.id);

        LOGGER.info("List existing entries for basic infos");
        basicInfos = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(BasicInfo.TYPE)).withPage(1).withSize(5));
        assertEquals(basicInfosCountBeforeCreate, basicInfos.getTotalCount());

        // DELETE : DRAFT VERSION
        LOGGER.info("Create again BasicInfo entry with key " + key);
        entry = service.createEntry(key, "Entry First Name", "Entry First Description", BasicInfo.TYPE);
        assertNotNull(entry);

        LOGGER.info("Create DRAFT Version for BasicInfo with key " + key);
        Map<String, ModelData> versionData = TestUtils.generateModelData(key, BasicInfo.TYPE, language);
        ModelVersion version = service.createVersion(entry.id, language, versionData);
        assertNotNull(version);

        LOGGER.info("Check DRAFT version exists");
        versions = service.getVersionHistoryForKey(key);
        assertEquals(1, versions.size());
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);

        LOGGER.info("Delete entry with key " + key);
        service.deleteEntry(entry.id);

        LOGGER.info("List existing entries for basic infos");
        basicInfos = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(BasicInfo.TYPE)).withPage(1).withSize(5));
        assertEquals(basicInfosCountBeforeCreate, basicInfos.getTotalCount());

        // DELETE : ACTIVE VERSION
        LOGGER.info("Create again BasicInfo entry with key " + key);
        entry = service.createEntry(key, "Entry First Name", "Entry First Description", BasicInfo.TYPE);
        assertNotNull(entry);

        LOGGER.info("Create DRAFT Version for BasicInfo with key " + key);
        versionData = TestUtils.generateModelData(key, BasicInfo.TYPE, language);
        version = service.createVersion(entry.id, language, versionData);
        assertNotNull(version);

        LOGGER.info("Activate version for BasicInfo with key " + key);
        service.updateVersionStatus(version.id, ModelVersion.Status.ACTIVE);

        LOGGER.info("Check ACTIVE version exists");
        versions = service.getVersionHistoryForKey(key);
        assertEquals(1, versions.size());
        assertEquals(ModelVersion.Status.ACTIVE, versions.get(0).status);

        LOGGER.info("Check deleting entry with ACTIVE versions is forbidden");
        String entryId = entry.id;
        assertThrows(ConsentManagerException.class, () -> service.deleteEntry(entryId));
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testModelVersionLifecycle() throws ConsentManagerException, EntityAlreadyExistsException, EntityNotFoundException, ModelDataSerializationException, InvalidStatusException {
        LOGGER.info("#### Test BasicInfo version lifecycle");
        String key = UUID.randomUUID().toString();
        String language = "fr";

        // CREATE
        LOGGER.info("Create entry");
        ModelEntry entry = service.createEntry(key, "Name " + key, "Description " + key, BasicInfo.TYPE);
        assertNotNull(entry);

        LOGGER.info("List versions");
        List<ModelVersion> versions = service.getVersionHistoryForEntry(entry.id);
        assertEquals(0, versions.size());

        LOGGER.info("Create BasicInfo data for key " + key);
        BasicInfo basicInfo = TestUtils.generateBasicInfo(key);
        ModelVersion version = service.createVersion(entry.id, language, Collections.singletonMap(language, basicInfo));

        // READ
        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entry.id);
        assertEquals(1, versions.size());
        assertEquals(version.id, versions.get(0).id);
        assertEquals(language, versions.get(0).availableLanguages);
        assertEquals(language, versions.get(0).defaultLanguage);
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);
        assertEquals(ModelVersion.Type.MINOR, versions.get(0).type);
        assertEquals("sheldon", versions.get(0).author);
        assertNotNull(versions.get(0).serial);
        assertEquals("sheldon", versions.get(0).content.get(language).author);
        BasicInfo data = (BasicInfo) versions.get(0).getData(language);
        assertEquals(basicInfo, data);

        // UPDATE
        LOGGER.info("Check that at this point, no version is active for key " + key);
        assertThrows(EntityNotFoundException.class, () -> service.findActiveVersionForKey(key));

        LOGGER.info("Change version type for key " + key);
        service.updateVersionType(version.id, ModelVersion.Type.MAJOR);

        LOGGER.info("Get version by id");
        version = service.getVersion(version.id);
        assertEquals(versions.get(0).id, version.id);
        assertEquals(ModelVersion.Status.DRAFT, version.status);
        assertEquals(ModelVersion.Type.MAJOR, version.type);

        LOGGER.info("Activate version for key " + key);
        service.updateVersionStatus(version.id, ModelVersion.Status.ACTIVE);

        LOGGER.info("Find active version for key " + key);
        version = service.findActiveVersionForKey(key);
        assertEquals(versions.get(0).id, version.id);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);
        assertEquals(ModelVersion.Type.MAJOR, version.type);

        LOGGER.info("Check version type cannot be changed for a version which is not DRAFT");
        String versionId = version.id;
        assertThrows(ConsentManagerException.class, () -> service.updateVersionType(versionId, ModelVersion.Type.MINOR));

        LOGGER.info("Change version content for key " + key);
        basicInfo.setTitle("New title");
        service.updateVersion(version.id, language, Collections.singletonMap(language, basicInfo));

        LOGGER.info("Check version content cannot be updated with a different type");
        assertThrows(ConsentManagerException.class, () -> service.updateVersion(versionId, language, Collections.singletonMap(language, TestUtils.generateConditions(key))));

        LOGGER.info("Check version content cannot be updated with an undeclared language");
        assertThrows(ConsentManagerException.class, () -> service.updateVersion(versionId, language, Collections.singletonMap("en", basicInfo)));

        LOGGER.info("Find version for serial");
        version = service.findVersionForSerial(version.serial);
        assertEquals(versions.get(0).id, version.id);
        LOGGER.info(version.toString());
        data = (BasicInfo) version.getData(language);
        assertEquals("New title", data.getTitle());

        // DELETE
        LOGGER.info("Create new version for key " + key);
        basicInfo.setTitle("New version title");
        basicInfo.setHeader("New version header");
        service.createVersion(entry.id, language, Collections.singletonMap(language, basicInfo));

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entry.id);
        assertEquals(2, versions.size());
        assertEquals(((BasicInfo) versions.get(0).getData(language)).getTitle(), "New title");
        assertEquals(((BasicInfo) versions.get(1).getData(language)).getTitle(), "New version title");

        LOGGER.info("Find latest version for key " + key);
        version = service.findLatestVersionForKey(key);
        assertEquals(versions.get(1).id, version.id);
        assertEquals(ModelVersion.Status.DRAFT, version.status);
        assertEquals(ModelVersion.Type.MINOR, version.type);

        LOGGER.info("Delete DRAFT version");
        service.deleteVersion(version.id);

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entry.id);
        assertEquals(1, versions.size());

        LOGGER.info("Find active version for key " + key);
        version = service.findActiveVersionForKey(key);
        assertEquals(versions.get(0).id, version.id);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

        LOGGER.info("Check deleting ACTIVE last version is forbidden");
        String oldVersionId = version.id;
        assertThrows(ConsentManagerException.class, () -> service.deleteVersion(oldVersionId));

        LOGGER.info("Create last version for key " + key);
        basicInfo.setTitle("Last version title");
        basicInfo.setHeader("Last version header");
        version = service.createVersion(entry.id, language, Collections.singletonMap(language, basicInfo));

        LOGGER.info("Activate last version for key " + key);
        service.updateVersionStatus(version.id, ModelVersion.Status.ACTIVE);

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entry.id);
        assertEquals(2, versions.size());
        assertEquals(ModelVersion.Status.ARCHIVED, versions.get(0).status);
        assertEquals(ModelVersion.Status.ACTIVE, versions.get(1).status);
        assertEquals(((BasicInfo) versions.get(0).getData(language)).getTitle(), "New title");
        assertEquals(((BasicInfo) versions.get(1).getData(language)).getTitle(), "Last version title");

        LOGGER.info("Check deleting a version which is not the last one is forbidden");
        assertThrows(ConsentManagerException.class, () -> service.deleteVersion(oldVersionId));

        LOGGER.info("Check changing the type of a version which is not the last one is forbidden");
        assertThrows(ConsentManagerException.class, () -> service.updateVersionType(oldVersionId, ModelVersion.Type.MAJOR));

        LOGGER.info("Check changing the status of a version which is not the last one is forbidden");
        assertThrows(ConsentManagerException.class, () -> service.updateVersionStatus(oldVersionId, ModelVersion.Status.ACTIVE));

        LOGGER.info("Check changing the content of a version which is not the last one is forbidden");
        basicInfo.setTitle("Different title");
        assertThrows(ConsentManagerException.class, () -> service.updateVersion(oldVersionId, language, Collections.singletonMap(language, basicInfo)));
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testEntryVersionsHistory() throws ConsentManagerException, EntityAlreadyExistsException, EntityNotFoundException, InvalidStatusException {
        LOGGER.info("#### Test BasicInfo versions history");
        LOGGER.info("Create entry");
        String key = UUID.randomUUID().toString();
        String entryId = service.createEntry(key, "Name " + key, "Description " + key, BasicInfo.TYPE).id;
        assertNotNull(entryId);

        LOGGER.info("List versions");
        List<ModelVersion> versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(0, versions.size());

        String language = "fr";
        LOGGER.info("Create BasicInfo " + key + " version 1");
        BasicInfo basicInfo = TestUtils.generateBasicInfo(key);
        String versionId = service.createVersion(entryId, language, Collections.singletonMap(language, basicInfo)).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(1, versions.size());
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);
        assertEquals("", versions.get(0).parent);
        assertEquals("", versions.get(0).child);

        LOGGER.info("Activate version 1");
        service.updateVersionStatus(versionId, ModelVersion.Status.ACTIVE);

        ModelVersion version = service.findActiveVersionForKey(key);
        assertEquals(versions.get(0), version);

        version = service.findLatestVersionForKey(key);
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

        LOGGER.info("Create BasicInfo " + key + " version 2");
        BasicInfo basicInfo2 = TestUtils.generateBasicInfo(key);
        String version2Id = service.createVersion(entryId, language, Collections.singletonMap(language, basicInfo2)).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(2, versions.size());
        assertEquals(ModelVersion.Status.ACTIVE, versions.get(0).status);
        assertEquals(ModelVersion.Status.DRAFT, versions.get(1).status);
        assertEquals("", versions.get(0).parent);
        assertEquals(versions.get(1).id, versions.get(0).child);
        assertEquals(versions.get(0).id, versions.get(1).parent);
        assertEquals("", versions.get(1).child);

        version = service.findActiveVersionForKey(key);
        assertEquals(versions.get(0), version);

        version = service.findLatestVersionForKey(key);
        assertEquals(versions.get(1), version);
        assertEquals(ModelVersion.Status.DRAFT, version.status);

        LOGGER.info("Activate version 2");
        service.updateVersionStatus(version2Id, ModelVersion.Status.ACTIVE);

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(2, versions.size());
        assertEquals(ModelVersion.Status.ARCHIVED, versions.get(0).status);
        assertEquals(ModelVersion.Status.ACTIVE, versions.get(1).status);

        version = service.findActiveVersionForKey(key);
        assertEquals(versions.get(1), version);

        version = service.findLatestVersionForKey(key);
        assertEquals(versions.get(1), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

        String version3Id = service.createVersion(entryId, language, Collections.singletonMap(language, basicInfo)).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(3, versions.size());
        assertEquals(ModelVersion.Status.ARCHIVED, versions.get(0).status);
        assertEquals(ModelVersion.Status.ACTIVE, versions.get(1).status);
        assertEquals(ModelVersion.Status.DRAFT, versions.get(2).status);
        assertEquals(versions.get(1).id, versions.get(2).parent);
        assertEquals(versions.get(2).id, versions.get(1).child);

        service.deleteVersion(version3Id);

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(2, versions.size());
        assertEquals(ModelVersion.Status.ARCHIVED, versions.get(0).status);
        assertEquals(ModelVersion.Status.ACTIVE, versions.get(1).status);
        assertEquals("", versions.get(1).child);
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testRecordLifecycle() throws TokenExpiredException, InvalidConsentException, InvalidTokenException, ConsentServiceException, EntityAlreadyExistsException, EntityNotFoundException, ConsentManagerException, InvalidStatusException {
        LOGGER.info("#### Test record lifecycle");
        List<String> types = new ArrayList<>();
        types.add(BasicInfo.TYPE);
        types.add(Processing.TYPE);
        CollectionPage<ModelEntry> entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        long entriesCount = entries.getTotalCount();
        String language = "fr";

        // Creating a BasicInfo entry
        String biKey = UUID.randomUUID().toString();
        ModelEntry ebi1 = service.createEntry(biKey, "Name " + biKey, "Description " + biKey, BasicInfo.TYPE);
        assertNotNull(ebi1);
        ModelVersion v1bi1 = service.createVersion(ebi1.id, language, Collections.singletonMap(language, TestUtils.generateBasicInfo(biKey)));
        service.updateVersionStatus(v1bi1.id, ModelVersion.Status.ACTIVE);

        // Creating a Processing entry
        String t1Key = UUID.randomUUID().toString();
        ModelEntry et1 = service.createEntry(t1Key, "Name " + t1Key, "Description " + t1Key, Processing.TYPE);
        assertNotNull(et1);
        ModelVersion v1t1 = service.createVersion(et1.id, language, Collections.singletonMap(language, TestUtils.generateProcessing(t1Key)));
        service.updateVersionStatus(v1t1.id, ModelVersion.Status.ACTIVE);

        // Creating another Processing entry
        String t2Key = UUID.randomUUID().toString();
        ModelEntry et2 = service.createEntry(t2Key, "Name " + t2Key, "Description " + t2Key, Processing.TYPE);
        assertNotNull(et2);
        ModelVersion v1t2 = service.createVersion(et2.id, language, Collections.singletonMap(language, TestUtils.generateProcessing(t2Key)));
        service.updateVersionStatus(v1t2.id, ModelVersion.Status.ACTIVE);

        // Checking that the entries have been created
        entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        assertEquals(entriesCount + 3, entries.getTotalCount());

        LOGGER.info("Creating READ context and token");
        String subject = "mmichu";
        ConsentContext readCtx = new ConsentContext()
                .setSubject(subject)
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLanguage(language)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        String readToken = service.buildToken(readCtx);

        LOGGER.info("Reading consent records before submit");
        Map<String, Record> records = service.systemListContextValidRecords(readCtx);
        assertEquals(0, records.size());

        LOGGER.info("First consent form");
        ConsentForm form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(0, form.getPreviousValues().size());

        LOGGER.info("Submitting first consent (creating record)");
        MultivaluedMap<String, String> values = new MultivaluedHashMap<>();
        values.putSingle("info", "element/basicinfo/" + biKey + "/" + v1bi1.serial);
        values.putSingle("element/processing/" + t1Key + "/" + v1t1.serial, "accepted");
        values.putSingle("element/processing/" + t2Key + "/" + v1t2.serial, "refused");
        service.submitConsent(form.getToken(), values);

        LOGGER.info("Reading consent records after submit");
        records = service.systemListContextValidRecords(readCtx);
        assertEquals(2, records.size());
        assertTrue(records.containsKey(et1.key));
        Record record = records.get(et1.key);
        assertEquals(v1t1.serial, record.bodySerial);
        assertEquals(Record.Status.VALID, record.status);
        assertEquals(Record.StatusExplanation.LATEST_VALID, record.statusExplanation);
        assertEquals("accepted", record.value);
        assertTrue(records.containsKey(et2.key));
        record = records.get(et2.key);
        assertEquals(v1t2.serial, record.bodySerial);
        assertEquals(Record.Status.VALID, record.status);
        assertEquals(Record.StatusExplanation.LATEST_VALID, record.statusExplanation);
        assertEquals("refused", record.value);

        LOGGER.info("Second consent form");
        form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(2, form.getPreviousValues().size());
        assertEquals("accepted", form.getPreviousValues().get(v1t1.serial));
        assertEquals("refused", form.getPreviousValues().get(v1t2.serial));

        LOGGER.info("Create new version of T2 (minor version)");
        ModelVersion v2t2 = service.createVersion(et2.id, language, Collections.singletonMap(language, TestUtils.generateProcessing("t2.2")));
        service.updateVersionType(v2t2.id, ModelVersion.Type.MINOR);
        service.updateVersionStatus(v2t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.info("Reading consent records after new version MINOR (no effect)");
        records = service.systemListContextValidRecords(readCtx);
        assertEquals(2, records.size());
        assertTrue(records.containsKey(et1.key));
        record = records.get(et1.key);
        assertEquals(v1t1.serial, record.bodySerial);
        assertEquals(Record.Status.VALID, record.status);
        assertEquals(Record.StatusExplanation.LATEST_VALID, record.statusExplanation);
        assertEquals("accepted", record.value);
        assertTrue(records.containsKey(et2.key));
        record = records.get(et2.key);
        assertEquals(v1t2.serial, record.bodySerial);
        assertEquals(Record.Status.VALID, record.status);
        assertEquals(Record.StatusExplanation.LATEST_VALID, record.statusExplanation);
        assertEquals("refused", record.value);

        LOGGER.info("Create new version of T2 (major version)");
        ModelVersion v3t2 = service.createVersion(et2.id, language, Collections.singletonMap(language, TestUtils.generateProcessing("t2.3")));
        service.updateVersionType(v3t2.id, ModelVersion.Type.MAJOR);
        service.updateVersionStatus(v3t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.info("Reading consent records after new version MAJOR (no more value for t2)");
        records = service.systemListContextValidRecords(readCtx);
        assertEquals(1, records.size());
        assertTrue(records.containsKey(et1.key));
        record = records.get(et1.key);
        assertEquals(v1t1.serial, record.bodySerial);
        assertEquals(Record.Status.VALID, record.status);
        assertEquals(Record.StatusExplanation.LATEST_VALID, record.statusExplanation);
        assertEquals("accepted", record.value);
        assertFalse(records.containsKey(et2.key));

        LOGGER.info("Third consent form");
        form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(1, form.getPreviousValues().size());
        assertEquals("accepted", form.getPreviousValues().get(v1t1.serial));

        LOGGER.info("New consent form");
        readCtx = new ConsentContext()
                .setSubject(subject)
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Collections.singletonList(t1Key))
                .setLanguage(language)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        readToken = service.buildToken(readCtx);
        form = service.generateForm(readToken);
        assertEquals(1, form.getElements().size());
        assertEquals(1, form.getPreviousValues().size());
        assertEquals("accepted", form.getPreviousValues().get(v1t1.serial));

        LOGGER.info("Submitting new consent (creating record)");
        values = new MultivaluedHashMap<>();
        values.putSingle("info", "element/basicinfo/" + biKey + "/" + v1bi1.serial);
        values.putSingle("element/processing/" + t1Key + "/" + v1t1.serial, "refused");
        service.submitConsent(form.getToken(), values);

        LOGGER.info("Reading consent records history for subject");
        Map<String, List<Record>> subjectRecords = service.listSubjectRecords(subject);
        assertEquals(2, subjectRecords.keySet().size());
        assertTrue(subjectRecords.containsKey(et1.key));
        assertEquals(2, subjectRecords.get(et1.key).size());
        assertTrue(subjectRecords.containsKey(et2.key));
        assertEquals(1, subjectRecords.get(et2.key).size());
        record = subjectRecords.get(et1.key).get(0);
        assertEquals(Record.Status.OBSOLETE, record.status);
        assertEquals(Record.StatusExplanation.OBSOLETE, record.statusExplanation);
        assertEquals("accepted", record.value);
        record = subjectRecords.get(et1.key).get(1);
        assertEquals(Record.Status.VALID, record.status);
        assertEquals(Record.StatusExplanation.LATEST_VALID, record.statusExplanation);
        assertEquals("refused", record.value);
        record = subjectRecords.get(et2.key).get(0);
        assertEquals(Record.Status.IRRELEVANT, record.status);
        assertEquals(Record.StatusExplanation.BODY_SERIAL_ARCHIVED, record.statusExplanation);
        assertEquals("refused", record.value);
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testSubjectLifecycle() throws TokenExpiredException, InvalidConsentException, InvalidTokenException, ConsentServiceException, EntityAlreadyExistsException, EntityNotFoundException, ConsentManagerException, InvalidStatusException {
        LOGGER.info("#### Test subject lifecycle");
        List<String> types = new ArrayList<>();
        types.add(BasicInfo.TYPE);
        types.add(Processing.TYPE);
        CollectionPage<ModelEntry> entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        long entriesCount = entries.getTotalCount();
        String language = "fr";

        // Creating a BasicInfo entry
        String biKey = UUID.randomUUID().toString();
        ModelEntry ebi1 = service.createEntry(biKey, "Name " + biKey, "Description " + biKey, BasicInfo.TYPE);
        assertNotNull(ebi1);
        ModelVersion v1bi1 = service.createVersion(ebi1.id, language, Collections.singletonMap(language, TestUtils.generateBasicInfo(biKey)));
        service.updateVersionStatus(v1bi1.id, ModelVersion.Status.ACTIVE);

        // Creating a Processing entry
        String t1Key = UUID.randomUUID().toString();
        ModelEntry et1 = service.createEntry(t1Key, "Name " + t1Key, "Description " + t1Key, Processing.TYPE);
        assertNotNull(et1);
        ModelVersion v1t1 = service.createVersion(et1.id, language, Collections.singletonMap(language, TestUtils.generateProcessing(t1Key)));
        service.updateVersionStatus(v1t1.id, ModelVersion.Status.ACTIVE);

        // Creating a Processing entry
        String t2Key = UUID.randomUUID().toString();
        ModelEntry et2 = service.createEntry(t2Key, "Name " + t2Key, "Description " + t2Key, Processing.TYPE);
        assertNotNull(et2);
        ModelVersion v1t2 = service.createVersion(et2.id, language, Collections.singletonMap(language, TestUtils.generateProcessing(t2Key)));
        service.updateVersionStatus(v1t2.id, ModelVersion.Status.ACTIVE);

        // Checking that the entries have been created
        entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        assertEquals(entriesCount + 3, entries.getTotalCount());
        String biIdentifier = "element/basicinfo/" + biKey + "/" + v1bi1.serial;
        String t1Identifier = "element/processing/" + t1Key + "/" + v1t1.serial;
        String t2Identifier = "element/processing/" + t2Key + "/" + v1t2.serial;

        LOGGER.info("Submitting a consent for sheldon");
        ConsentContext ctx = new ConsentContext()
                .setSubject("sheldon")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLanguage(language)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        String token = service.buildToken(ctx);
        ConsentForm form = service.generateForm(token);
        MultivaluedMap<String, String> values = new MultivaluedHashMap<>();
        values.putSingle("info", biIdentifier);
        values.putSingle(t1Identifier, "accepted");
        values.putSingle(t2Identifier, "refused");
        service.submitConsent(form.getToken(), values);

        LOGGER.info("Submitting a consent for penny");
        ctx = new ConsentContext()
                .setSubject("penny")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLanguage(language)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        token = service.buildToken(ctx);
        form = service.generateForm(token);
        values = new MultivaluedHashMap<>();
        values.putSingle("info", biIdentifier);
        values.putSingle(t1Identifier, "refused");
        values.putSingle(t2Identifier, "refused");
        service.submitConsent(form.getToken(), values);

        LOGGER.info("Submitting a consent for leonard");
        ctx = new ConsentContext()
                .setSubject("leonard")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLanguage(language)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        token = service.buildToken(ctx);
        form = service.generateForm(token);
        values = new MultivaluedHashMap<>();
        values.putSingle("info", biIdentifier);
        values.putSingle(t1Identifier, "accepted");
        values.putSingle(t2Identifier, "accepted");
        service.submitConsent(form.getToken(), values);

        LOGGER.info("Testing find subjects");
        List<Subject> subjects = service.findSubjects("e");
        assertEquals(3, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> "sheldon".equals(s.name)));
        assertTrue(subjects.stream().anyMatch(s -> "penny".equals(s.name)));
        assertTrue(subjects.stream().anyMatch(s -> "leonard".equals(s.name)));

        subjects = service.findSubjects("on");
        assertEquals(2, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> "sheldon".equals(s.name)));
        assertTrue(subjects.stream().anyMatch(s -> "leonard".equals(s.name)));

        subjects = service.findSubjects("penny");
        assertEquals(1, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> "penny".equals(s.name)));

        subjects = service.findSubjects("rajesh");
        assertEquals(0, subjects.size());

        LOGGER.info("Testing Create subject");
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("rajesh");
        subjectDto.setEmailAddress("rajesh@localhost");
        Subject subject = service.createSubject(subjectDto);
        assertNotNull(subject.id);
        assertNotNull(subject.owner);
        assertEquals("rajesh", subject.name);
        assertEquals("rajesh@localhost", subject.emailAddress);
        assertTrue(subject.creationTimestamp > 0);

        LOGGER.info("Testing Create subject with name already taken");
        subjectDto.setEmailAddress("duplicate@localhost");
        assertThrows(ConsentManagerException.class, () -> service.createSubject(subjectDto));

        LOGGER.info("Testing Get new subject");
        subject = service.getSubject("stuart");
        assertNull(subject.id);
        assertNull(subject.owner);
        assertEquals("stuart", subject.name);
        assertNull(subject.emailAddress);
        assertEquals(0, subject.creationTimestamp);

        LOGGER.info("Testing Get known subject");
        subject = service.getSubject("rajesh");
        assertNotNull(subject.id);
        assertNotNull(subject.owner);
        assertEquals("rajesh", subject.name);
        assertEquals("rajesh@localhost", subject.emailAddress);
        assertTrue(subject.creationTimestamp > 0);
        String subjectId = subject.id;

        LOGGER.info("Testing Update subject");
        subjectDto.setEmailAddress("new.rajesh@localhost");
        subject = service.updateSubject(subjectId, subjectDto);
        assertNotNull(subject.id);
        assertNotNull(subject.owner);
        assertEquals("rajesh", subject.name);
        assertEquals("new.rajesh@localhost", subject.emailAddress);
        assertTrue(subject.creationTimestamp > 0);

        LOGGER.info("Testing Update subject with name changed");
        subjectDto.setName("stuart");
        assertThrows(ConsentManagerException.class, () -> service.updateSubject(subjectId, subjectDto));

        LOGGER.info("Testing Update subject with unknown id");
        assertThrows(EntityNotFoundException.class, () -> service.updateSubject(UUID.randomUUID().toString(), subjectDto));
    }

}
