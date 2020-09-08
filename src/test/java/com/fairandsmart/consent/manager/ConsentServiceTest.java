package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.TestUtils;
import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Treatment;
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
    public void testCreateEntryForExistingKey() throws EntityAlreadyExistsException {
        LOGGER.info("#### Test create entry for existing key");
        final String key = UUID.randomUUID().toString();
        ModelEntry entry = service.createEntry(key, "header1", "description1", Header.TYPE);
        assertNotNull(entry);
        assertThrows(EntityAlreadyExistsException.class, () -> service.createEntry(key, "header2", "description2", Header.TYPE));
    }

    @Test
    @Transactional
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testCreateAndUpdateHeaderEntry() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.info("#### Test create and Update Header entry");
        LOGGER.info("List existing entries for headers");
        CollectionPage<ModelEntry> headers = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(Header.TYPE)).withPage(1).withSize(5));
        long headersCountBeforeCreate = headers.getTotalCount();

        String key = UUID.randomUUID().toString();
        LOGGER.info("Create header entry with key " + key);
        ModelEntry entry = service.createEntry(key, "Entry First Name", "Entry First Description", Header.TYPE);
        assertNotNull(entry);

        LOGGER.info("List existing entries for headers");
        headers = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(Header.TYPE)).withPage(1).withSize(5));
        assertEquals(headersCountBeforeCreate + 1, headers.getTotalCount());

        LOGGER.info("Lookup entry with key " + key);
        entry = service.findEntryForKey(key);
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertNotNull(entry.id);
        assertNotNull(entry.owner);
        assertEquals(Header.TYPE, entry.type);
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
        LOGGER.info("#### Test create and Update Header content");
        LOGGER.info("Create entry");
        String key = UUID.randomUUID().toString();
        String entryId = service.createEntry(key, "Name " + key, "Description " + key, Header.TYPE).id;
        assertNotNull(entryId);

        LOGGER.info("List versions");
        List<ModelVersion> versions = service.getVersionHistoryForKey(entryId);
        assertEquals(0, versions.size());

        String locale = "fr_FR";
        LOGGER.info("Create Header " + key);
        Header header = TestUtils.generateHeader(key);
        String versionId = service.createVersion(entryId, locale, header).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(1, versions.size());
        assertEquals(locale, versions.get(0).availableLocales);
        assertEquals(locale, versions.get(0).defaultLocale);
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);
        assertEquals("sheldon", versions.get(0).author);
        assertNotNull(versions.get(0).serial);
        assertEquals("sheldon", versions.get(0).content.get(locale).author);
        Header data = (Header) versions.get(0).getData(locale);
        assertEquals(header, data);

        ModelVersion version = service.findVersionForSerial(versions.get(0).serial);
        assertEquals(versions.get(0), version);
        //At this point no version is active
        assertThrows(EntityNotFoundException.class, () -> service.findActiveVersionForKey(key));

        LOGGER.log(Level.INFO, "Activate entry");
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
    public void testCreateAndReadRecord() throws TokenExpiredException, InvalidConsentException, InvalidTokenException, ConsentServiceException, EntityAlreadyExistsException, EntityNotFoundException, ConsentManagerException, InvalidStatusException {
        LOGGER.info("Listing existing entries");
        List<String> types = new ArrayList<>();
        types.add(Header.TYPE);
        types.add(Treatment.TYPE);
        types.add(Footer.TYPE);
        CollectionPage<ModelEntry> entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        long entriesCount = entries.getTotalCount();

        LOGGER.log(Level.INFO, "Creating, updating and activating entries");
        String locale = "fr_FR";

        String hKey = UUID.randomUUID().toString();
        ModelEntry eh1 = service.createEntry(hKey, "Name " + hKey, "Description " + hKey, Header.TYPE);
        assertNotNull(eh1);
        ModelVersion v1h1 = service.createVersion(eh1.id, locale, TestUtils.generateHeader(hKey));
        service.updateVersionStatus(v1h1.id, ModelVersion.Status.ACTIVE);

        String t1Key = UUID.randomUUID().toString();
        ModelEntry et1 = service.createEntry(t1Key, "Name " + t1Key, "Description " + t1Key, Treatment.TYPE);
        assertNotNull(et1);
        ModelVersion v1t1 = service.createVersion(et1.id, locale, TestUtils.generateTreatment(t1Key));
        service.updateVersionStatus(v1t1.id, ModelVersion.Status.ACTIVE);

        String t2Key = UUID.randomUUID().toString();
        ModelEntry et2 = service.createEntry(t2Key, "Name " + t2Key, "Description " + t2Key, Treatment.TYPE);
        assertNotNull(et2);
        ModelVersion v1t2 = service.createVersion(et2.id, locale, TestUtils.generateTreatment(t2Key));
        service.updateVersionStatus(v1t2.id, ModelVersion.Status.ACTIVE);

        String fKey = UUID.randomUUID().toString();
        ModelEntry ef1 = service.createEntry(fKey, "Name " + fKey, "Description " + fKey, Footer.TYPE);
        assertNotNull(ef1);
        ModelVersion v1f1 = service.createVersion(ef1.id, locale, TestUtils.generateFooter(fKey));
        service.updateVersionStatus(v1f1.id, ModelVersion.Status.ACTIVE);

        LOGGER.info("Listing existing entries");
        entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        assertEquals(entriesCount + 4, entries.getTotalCount());

        LOGGER.log(Level.INFO, "Creating READ context and token");
        ConsentContext readCtx = new ConsentContext()
                .setSubject("mmichu")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader(hKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setFooter(fKey)
                .setLocale(locale)
                .setCollectionMethod(ConsentContext.CollectionMethod.WEBFORM);
        String readToken = service.buildToken(readCtx);

        LOGGER.log(Level.INFO, "Reading consent records before submit");
        List<Record> records = service.findRecordsForContext(readCtx);
        assertEquals(0, records.size());

        LOGGER.log(Level.INFO, "First consent form");
        ConsentForm form = service.generateForm(readToken, null);
        assertEquals(2, form.getElements().size());
        assertEquals(0, form.getPreviousValues().size());

        LOGGER.log(Level.INFO, "Submitting first consent (creating record)");
        MultivaluedMap<String, String> values = new MultivaluedHashMap<>();
        values.putSingle("header", "element/header/" + hKey + "/" + v1h1.serial);
        values.putSingle("element/treatment/" + t1Key + "/" + v1t1.serial, "accepted");
        values.putSingle("element/treatment/" + t2Key + "/" + v1t2.serial, "refused");
        values.putSingle("footer", "element/footer/" + fKey + "/" + v1f1.serial);
        service.submitConsent(form.getToken(), values);

        LOGGER.log(Level.INFO, "Reading consent records after submit");
        records = service.findRecordsForContext(readCtx);
        assertEquals(2, records.size());
        assertEquals(1, records.stream().filter(r -> r.bodySerial.equals(v1t1.serial)).count());
        assertEquals(1, records.stream().filter(r -> r.bodySerial.equals(v1t2.serial)).count());

        LOGGER.log(Level.INFO, "Second consent form");
        form = service.generateForm(readToken, null);
        assertEquals(2, form.getElements().size());
        assertEquals(2, form.getPreviousValues().size());

        LOGGER.log(Level.INFO, "Create new version of T2 (minor version)");
        ModelVersion v2t2 = service.createVersion(et2.id, locale, TestUtils.generateTreatment("t2.2"));
        service.updateVersionType(v2t2.id, ModelVersion.Type.MINOR);
        service.updateVersionStatus(v2t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.log(Level.INFO, "Reading consent records after new version MINOR (no effect)");
        records = service.findRecordsForContext(readCtx);
        assertEquals(2, records.size());
        assertEquals(1, records.stream().filter(r -> r.bodySerial.equals(v1t1.serial)).count());
        assertEquals(1, records.stream().filter(r -> r.bodySerial.equals(v1t2.serial)).count());

        LOGGER.log(Level.INFO, "Create new version of T2 (major version)");
        ModelVersion v3t2 = service.createVersion(et2.id, locale, TestUtils.generateTreatment("t2.3"));
        service.updateVersionType(v3t2.id, ModelVersion.Type.MAJOR);
        service.updateVersionStatus(v3t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.log(Level.INFO, "Reading consent records after new version MAJOR (no more value for t2)");
        records = service.findRecordsForContext(readCtx);
        assertEquals(1, records.size());
        assertEquals(1, records.stream().filter(r -> r.bodySerial.equals(v1t1.serial)).count());
        assertEquals(0, records.stream().filter(r -> r.bodySerial.equals(v1t2.serial)).count());
    }

}
