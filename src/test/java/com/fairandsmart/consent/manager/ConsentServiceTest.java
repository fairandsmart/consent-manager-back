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
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ConsentServiceTest {

    private static final Logger LOGGER = Logger.getLogger(ConsentServiceTest.class.getName());

    //private static String TEST_USER = "sheldon";

    @Inject
    ConsentService service;

    /*
    @BeforeAll
    public static void setup() {
        LOGGER.info("Applying mock to Authentication Service");
        AuthenticationServiceBean mock = Mockito.mock(AuthenticationServiceBean.class);
        Mockito.when(mock.getConnectedIdentifier()).thenReturn(TEST_USER);
        Mockito.when(mock.isConnectedIdentifierAdmin()).thenReturn(true);
        Mockito.when(mock.isConnectedIdentifierOperator()).thenReturn(true);
        QuarkusMock.installMockForType(mock, AuthenticationServiceBean.class);
    }
    */

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testCreateEntryForExistingKey() throws EntityAlreadyExistsException, AccessDeniedException {
        LOGGER.info("#### Test create entry for existing key");
        final String key = UUID.randomUUID().toString();
        ModelEntry entry = service.createEntry(key, "header1", "description1", BasicInfo.TYPE);
        assertNotNull(entry);
        assertThrows(EntityAlreadyExistsException.class, () -> service.createEntry(key, "header2", "description2", BasicInfo.TYPE));
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testCreateAndUpdateHeaderEntry() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.info("#### Test create and update Header entry");
        LOGGER.info("List existing entries for headers");
        CollectionPage<ModelEntry> headers = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(BasicInfo.TYPE)).withPage(1).withSize(5));
        long headersCountBeforeCreate = headers.getTotalCount();

        String key = UUID.randomUUID().toString();
        LOGGER.info("Create header entry with key " + key);
        ModelEntry entry = service.createEntry(key, "Entry First Name", "Entry First Description", BasicInfo.TYPE);
        assertNotNull(entry);

        LOGGER.info("List existing entries for headers");
        headers = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(BasicInfo.TYPE)).withPage(1).withSize(5));
        assertEquals(headersCountBeforeCreate + 1, headers.getTotalCount());

        LOGGER.info("Lookup entry with key " + key);
        entry = service.findEntryForKey(key);
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertNotNull(entry.id);
        assertNotNull(entry.owner);
        assertEquals(BasicInfo.TYPE, entry.type);
        assertEquals(key, entry.key);
        assertEquals("Entry First Name", entry.name);
        assertEquals("Entry First Description", entry.description);

        LOGGER.info("Update entry with key " + key);
        service.updateEntry(entry.id, "Entry Name Updated", "Entry Description Updated");
        entry = service.findEntryForKey(key);
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertEquals("Entry Name Updated", entry.name);
        assertEquals("Entry Description Updated", entry.description);

        String fakeKey = UUID.randomUUID().toString();
        assertThrows(EntityNotFoundException.class, () -> service.updateEntry(fakeKey, "Entry Name Updated", "Entry Description Updated"));

        LOGGER.info("Check no version exists");
        List<ModelVersion> versions = service.getVersionHistoryForKey(entry.id);
        assertEquals(0, versions.size());
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testCreateAndUpdateHeaderContent() throws ConsentManagerException, EntityAlreadyExistsException, EntityNotFoundException, ModelDataSerializationException, InvalidStatusException {
        LOGGER.info("#### Test create and update Header content");
        LOGGER.info("Create entry");
        String key = UUID.randomUUID().toString();
        String entryId = service.createEntry(key, "Name " + key, "Description " + key, BasicInfo.TYPE).id;
        assertNotNull(entryId);

        LOGGER.info("List versions");
        List<ModelVersion> versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(0, versions.size());

        String locale = "fr_FR";
        LOGGER.info("Create Header " + key);
        BasicInfo header = TestUtils.generateBasicInfo(key);
        String versionId = service.createVersion(entryId, locale, Collections.singletonMap(locale, header)).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(1, versions.size());
        assertEquals(locale, versions.get(0).availableLocales);
        assertEquals(locale, versions.get(0).defaultLocale);
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);
        assertEquals("sheldon", versions.get(0).author);
        assertNotNull(versions.get(0).serial);
        assertEquals("sheldon", versions.get(0).content.get(locale).author);
        BasicInfo data = (BasicInfo) versions.get(0).getData(locale);
        assertEquals(header, data);

        ModelVersion version = service.findVersionForSerial(versions.get(0).serial);
        assertEquals(versions.get(0), version);
        //At this point no version is active
        assertThrows(EntityNotFoundException.class, () -> service.findActiveVersionForKey(key));

        LOGGER.log(Level.INFO, "Activate version");
        service.updateVersionStatus(versionId, ModelVersion.Status.ACTIVE);

        version = service.findActiveVersionForKey(key);
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

        version = service.findLatestVersionForKey(key);
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testHeaderVersionHistory() throws ConsentManagerException, EntityAlreadyExistsException, EntityNotFoundException, InvalidStatusException {
        LOGGER.info("#### Test Header versions history");
        LOGGER.info("Create entry");
        String key = UUID.randomUUID().toString();
        String entryId = service.createEntry(key, "Name " + key, "Description " + key, BasicInfo.TYPE).id;
        assertNotNull(entryId);

        LOGGER.info("List versions");
        List<ModelVersion> versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(0, versions.size());

        String locale = "fr_FR";
        LOGGER.info("Create Header " + key + " version 1");
        BasicInfo header = TestUtils.generateBasicInfo(key);
        String versionId = service.createVersion(entryId, locale, Collections.singletonMap(locale, header)).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(1, versions.size());
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);
        assertEquals("", versions.get(0).parent);
        assertEquals("", versions.get(0).child);

        LOGGER.log(Level.INFO, "Activate version 1");
        service.updateVersionStatus(versionId, ModelVersion.Status.ACTIVE);

        ModelVersion version = service.findActiveVersionForKey(key);
        assertEquals(versions.get(0), version);

        version = service.findLatestVersionForKey(key);
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

        LOGGER.info("Create Header " + key + " version 2");
        BasicInfo header2 = TestUtils.generateBasicInfo(key);
        String version2Id = service.createVersion(entryId, locale, Collections.singletonMap(locale, header2)).id;

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

        LOGGER.log(Level.INFO, "Activate version 2");
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

        String version3Id = service.createVersion(entryId, locale, Collections.singletonMap(locale, header)).id;

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
    public void testCreateAndReadRecord() throws TokenExpiredException, InvalidConsentException, InvalidTokenException, ConsentServiceException, EntityAlreadyExistsException, EntityNotFoundException, ConsentManagerException, InvalidStatusException {
        LOGGER.info("Listing Create and Read records");
        List<String> types = new ArrayList<>();
        types.add(BasicInfo.TYPE);
        types.add(Processing.TYPE);
        CollectionPage<ModelEntry> entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        long entriesCount = entries.getTotalCount();
        String locale = "fr_FR";
        String biKey = UUID.randomUUID().toString();
        ModelEntry ebi1 = service.createEntry(biKey, "Name " + biKey, "Description " + biKey, BasicInfo.TYPE);
        assertNotNull(ebi1);
        ModelVersion v1bi1 = service.createVersion(ebi1.id, locale, Collections.singletonMap(locale, TestUtils.generateBasicInfo(biKey)));
        service.updateVersionStatus(v1bi1.id, ModelVersion.Status.ACTIVE);
        String t1Key = UUID.randomUUID().toString();
        ModelEntry et1 = service.createEntry(t1Key, "Name " + t1Key, "Description " + t1Key, Processing.TYPE);
        assertNotNull(et1);
        ModelVersion v1t1 = service.createVersion(et1.id, locale, Collections.singletonMap(locale, TestUtils.generateProcessing(t1Key)));
        service.updateVersionStatus(v1t1.id, ModelVersion.Status.ACTIVE);
        String t2Key = UUID.randomUUID().toString();
        ModelEntry et2 = service.createEntry(t2Key, "Name " + t2Key, "Description " + t2Key, Processing.TYPE);
        assertNotNull(et2);
        ModelVersion v1t2 = service.createVersion(et2.id, locale, Collections.singletonMap(locale, TestUtils.generateProcessing(t2Key)));
        service.updateVersionStatus(v1t2.id, ModelVersion.Status.ACTIVE);
        String fKey = UUID.randomUUID().toString();
        entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        assertEquals(entriesCount + 3, entries.getTotalCount());



        LOGGER.log(Level.INFO, "Creating READ context and token");
        ConsentContext readCtx = new ConsentContext()
                .setSubject("mmichu")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLocale(locale)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        String readToken = service.buildToken(readCtx);

        LOGGER.log(Level.INFO, "Reading consent records before submit");
        Map<String, Record> records = service.systemListContextValidRecords(readCtx);
        assertEquals(0, records.size());

        LOGGER.log(Level.INFO, "First consent form");
        ConsentForm form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(0, form.getPreviousValues().size());

        LOGGER.log(Level.INFO, "Submitting first consent (creating record)");
        MultivaluedMap<String, String> values = new MultivaluedHashMap<>();
        values.putSingle("info", "element/basicinfo/" + biKey + "/" + v1bi1.serial);
        values.putSingle("element/processing/" + t1Key + "/" + v1t1.serial, "accepted");
        values.putSingle("element/processing/" + t2Key + "/" + v1t2.serial, "refused");
        service.submitConsent(form.getToken(), values);

        LOGGER.log(Level.INFO, "Reading consent records after submit");
        records = service.systemListContextValidRecords(readCtx);
        assertEquals(2, records.size());
        assertTrue(records.containsKey(et1.key));
        assertEquals(v1t1.serial, records.get(et1.key).bodySerial);
        assertTrue(records.containsKey(et2.key));
        assertEquals(v1t2.serial, records.get(et2.key).bodySerial);

        LOGGER.log(Level.INFO, "Second consent form");
        form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(2, form.getPreviousValues().size());

        LOGGER.log(Level.INFO, "Create new version of T2 (minor version)");
        ModelVersion v2t2 = service.createVersion(et2.id, locale, Collections.singletonMap(locale, TestUtils.generateProcessing("t2.2")));
        service.updateVersionType(v2t2.id, ModelVersion.Type.MINOR);
        service.updateVersionStatus(v2t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.log(Level.INFO, "Reading consent records after new version MINOR (no effect)");
        records = service.systemListContextValidRecords(readCtx);
        assertEquals(2, records.size());
        assertTrue(records.containsKey(et1.key));
        assertEquals(v1t1.serial, records.get(et1.key).bodySerial);
        assertTrue(records.containsKey(et2.key));
        assertEquals(v1t2.serial, records.get(et2.key).bodySerial);

        LOGGER.log(Level.INFO, "Create new version of T2 (major version)");
        ModelVersion v3t2 = service.createVersion(et2.id, locale, Collections.singletonMap(locale, TestUtils.generateProcessing("t2.3")));
        service.updateVersionType(v3t2.id, ModelVersion.Type.MAJOR);
        service.updateVersionStatus(v3t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.log(Level.INFO, "Reading consent records after new version MAJOR (no more value for t2)");
        records = service.systemListContextValidRecords(readCtx);
        assertEquals(1, records.size());
        assertTrue(records.containsKey(et1.key));
        assertEquals(v1t1.serial, records.get(et1.key).bodySerial);
        assertFalse(records.containsKey(et2.key));
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testFindSubjects() throws TokenExpiredException, InvalidConsentException, InvalidTokenException, ConsentServiceException, EntityAlreadyExistsException, EntityNotFoundException, ConsentManagerException, InvalidStatusException {
        LOGGER.info("Testing find subjects");
        List<String> types = new ArrayList<>();
        types.add(BasicInfo.TYPE);
        types.add(Processing.TYPE);
        CollectionPage<ModelEntry> entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        long entriesCount = entries.getTotalCount();
        String locale = "fr_FR";
        String biKey = UUID.randomUUID().toString();
        ModelEntry ebi1 = service.createEntry(biKey, "Name " + biKey, "Description " + biKey, BasicInfo.TYPE);
        assertNotNull(ebi1);
        ModelVersion v1bi1 = service.createVersion(ebi1.id, locale, Collections.singletonMap(locale, TestUtils.generateBasicInfo(biKey)));
        service.updateVersionStatus(v1bi1.id, ModelVersion.Status.ACTIVE);
        String t1Key = UUID.randomUUID().toString();
        ModelEntry et1 = service.createEntry(t1Key, "Name " + t1Key, "Description " + t1Key, Processing.TYPE);
        assertNotNull(et1);
        ModelVersion v1t1 = service.createVersion(et1.id, locale, Collections.singletonMap(locale, TestUtils.generateProcessing(t1Key)));
        service.updateVersionStatus(v1t1.id, ModelVersion.Status.ACTIVE);
        String t2Key = UUID.randomUUID().toString();
        ModelEntry et2 = service.createEntry(t2Key, "Name " + t2Key, "Description " + t2Key, Processing.TYPE);
        assertNotNull(et2);
        ModelVersion v1t2 = service.createVersion(et2.id, locale, Collections.singletonMap(locale, TestUtils.generateProcessing(t2Key)));
        service.updateVersionStatus(v1t2.id, ModelVersion.Status.ACTIVE);
        String fKey = UUID.randomUUID().toString();
        entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        assertEquals(entriesCount + 3, entries.getTotalCount());

        LOGGER.log(Level.INFO, "Submitting a consent for sheldon");
        ConsentContext ctx = new ConsentContext()
                .setSubject("sheldon")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLocale(locale)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        String token = service.buildToken(ctx);
        ConsentForm form = service.generateForm(token);
        MultivaluedMap<String, String> values = new MultivaluedHashMap<>();
        values.putSingle("info", "element/basicinfo/" + biKey + "/" + v1bi1.serial);
        values.putSingle("element/processing/" + t1Key + "/" + v1t1.serial, "accepted");
        values.putSingle("element/processing/" + t2Key + "/" + v1t2.serial, "refused");
        service.submitConsent(form.getToken(), values);

        LOGGER.log(Level.INFO, "Submitting a consent for penny");
        ctx = new ConsentContext()
                .setSubject("penny")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLocale(locale)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        token = service.buildToken(ctx);
        form = service.generateForm(token);
        values = new MultivaluedHashMap<>();
        values.putSingle("info", "element/basicinfo/" + biKey + "/" + v1bi1.serial);
        values.putSingle("element/processing/" + t1Key + "/" + v1t1.serial, "refused");
        values.putSingle("element/processing/" + t2Key + "/" + v1t2.serial, "refused");
        service.submitConsent(form.getToken(), values);

        LOGGER.log(Level.INFO, "Submitting a consent for leonard");
        ctx = new ConsentContext()
                .setSubject("leonard")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setLocale(locale)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        token = service.buildToken(ctx);
        form = service.generateForm(token);
        values = new MultivaluedHashMap<>();
        values.putSingle("info", "element/basicinfo/" + biKey + "/" + v1bi1.serial);
        values.putSingle("element/processing/" + t1Key + "/" + v1t1.serial, "accepted");
        values.putSingle("element/processing/" + t2Key + "/" + v1t2.serial, "accepted");
        service.submitConsent(form.getToken(), values);

        List<String> subjects = service.findSubjects("e");
        assertEquals(3, subjects.size());
        assertTrue(subjects.contains("sheldon"));
        assertTrue(subjects.contains("penny"));
        assertTrue(subjects.contains("leonard"));

        subjects = service.findSubjects("on");
        assertEquals(2, subjects.size());
        assertTrue(subjects.contains("sheldon"));
        assertTrue(subjects.contains("leonard"));

        subjects = service.findSubjects("penny");
        assertEquals(1, subjects.size());
        assertTrue(subjects.contains("penny"));

        subjects = service.findSubjects("rajesh");
        assertEquals(0, subjects.size());

    }

}
