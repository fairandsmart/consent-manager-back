package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.model.Controller;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Treatment;
import com.fairandsmart.consent.security.AuthenticationServiceBean;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ConsentServiceTest {

    private static final Logger LOGGER = Logger.getLogger(ConsentServiceTest.class.getName());
    private static final String TEST_USER = "sheldon";


    @Inject
    ConsentService service;

    @BeforeAll
    public static void setup() {
        LOGGER.info("Applying mock to Authentication Service");
        AuthenticationServiceBean mock = Mockito.mock(AuthenticationServiceBean.class);
        Mockito.when(mock.getConnectedIdentifier()).thenReturn(TEST_USER);
        QuarkusMock.installMockForType(mock, AuthenticationServiceBean.class);
    }

    @Test
    public void testCreateEntryForExistingKey() throws EntityAlreadyExistsException {
        ModelEntry entry = service.createEntry("existing", "header1", "description", "header");
        assertNotNull(entry);
        assertThrows(EntityAlreadyExistsException.class, () -> {
            service.createEntry("existing", "header1", "description", "header");
        });
    }

    @Test
    @Transactional
    public void testCreateAndUpdateHeaderEntry() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.info("List existing entries for headers");
        CollectionPage<ModelEntry> headers = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(Header.TYPE)).withPage(1).withSize(5));
        long headersCountBeforeCreate = headers.getTotalCount();

        ModelEntry entry = service.createEntry("e1", "entry1", "Description de entry1", Header.TYPE);
        assertNotNull(entry);

        LOGGER.info("List existing entries for headers");
        headers = service.listEntries(new ModelFilter().withTypes(Collections.singletonList(Header.TYPE)).withPage(1).withSize(5));
        assertEquals(headersCountBeforeCreate + 1, headers.getTotalCount());

        LOGGER.info("Lookup entry e1 by key");
        entry = service.findEntryForKey("e1");
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertNotNull(entry.id);
        assertNotNull(entry.owner);
        assertEquals(Header.TYPE, entry.type);
        assertEquals("e1", entry.key);
        assertEquals("entry1", entry.name);
        assertEquals("Description de entry1", entry.description);

        LOGGER.info("Update entry with key e1");
        service.updateEntry(entry.id, "Entry Name Updated", "Entry Description Updated");
        entry = service.findEntryForKey("e1");
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertEquals("Entry Name Updated", entry.name);
        assertEquals("Entry Description Updated", entry.description);

        String fakeid = UUID.randomUUID().toString();
        assertThrows(EntityNotFoundException.class, () -> {
            service.updateEntry(fakeid, "Entry Name Updated", "Entry Description Updated");
        });

        LOGGER.info("Check no version exists");
        List<ModelVersion> versions = service.getVersionHistoryForKey(entry.id);
        assertEquals(0, versions.size());
    }

    @Test
    @Transactional
    public void testCreateAndUpdateHeaderContent() throws ConsentManagerException, EntityAlreadyExistsException, EntityNotFoundException, ModelDataSerializationException, InvalidStatusException {
        LOGGER.info("Create entry");
        String entryId = service.createEntry("h10", "header10", "Description de header 10", Header.TYPE).id;
        assertNotNull(entryId);

        LOGGER.info("List versions");
        List<ModelVersion> versions = service.getVersionHistoryForKey(entryId);
        assertEquals(0, versions.size());

        LOGGER.info("Create Header h10");
        Controller controller = new Controller();
        controller.setName("Name");
        controller.setCompany("Company");
        controller.setActingBehalfCompany(true);
        controller.setAddress("Address");
        controller.setEmail("Email");
        controller.setPhoneNumber("Phone Number");
        Header header = new Header();
        header.setTitle("title");
        header.setBody("body");
        header.setPrivacyPolicyUrl("readmorelink");
        header.setCustomPrivacyPolicyText("View Privacy Policy");
        header.setCollectionMethod("Web Form");
        header.setJurisdiction("France");
        header.setShortNoticeLink("Short Notice Link");
        header.setScope("Scope");
        header.setLogoAltText("Logo Alt Text");
        header.setLogoPath("Logo path");
        header.setDataController(controller);
        header.setShowCollectionMethod(true);
        header.setShowDataController(true);
        header.setShowShortNoticeLink(true);
        header.setShowScope(true);
        header.setShowJurisdiction(true);
        String versionId = service.createVersion(entryId, "fr_FR", header).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(1, versions.size());
        assertEquals("fr_FR", versions.get(0).availableLocales);
        assertEquals("fr_FR", versions.get(0).defaultLocale);
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);
        assertEquals(TEST_USER, versions.get(0).owner);
        assertNotNull(versions.get(0).serial);
        assertEquals(TEST_USER, versions.get(0).content.get("fr_FR").author);
        Header data = (Header) versions.get(0).getData("fr_FR");
        assertEquals("title", data.getTitle());
        assertEquals("body", data.getBody());
        assertEquals("readmorelink", data.getPrivacyPolicyUrl());
        assertEquals("View Privacy Policy", data.getCustomPrivacyPolicyText());
        assertEquals("Web Form", data.getCollectionMethod());
        assertEquals("France", data.getJurisdiction());
        assertEquals("Short Notice Link", data.getShortNoticeLink());
        assertEquals("Scope", data.getScope());
        assertEquals("Logo Alt Text", data.getLogoAltText());
        assertEquals("Logo path", data.getLogoPath());
        assertEquals(true, data.isShowCollectionMethod());
        assertEquals(true, data.isShowDataController());
        assertEquals(true, data.isShowJurisdiction());
        assertEquals(true, data.isShowScope());
        assertEquals(true, data.isShowShortNoticeLink());
        assertEquals("Name", data.getDataController().getName());
        assertEquals("Company", data.getDataController().getCompany());
        assertEquals(true, data.getDataController().isActingBehalfCompany());
        assertEquals("Address", data.getDataController().getAddress());
        assertEquals("Email", data.getDataController().getEmail());
        assertEquals("Phone Number", data.getDataController().getPhoneNumber());

        ModelVersion version = service.findVersionForSerial(versions.get(0).serial);
        assertEquals(versions.get(0), version);

        //At this point no version is active
        assertThrows(EntityNotFoundException.class, () -> {
            service.findActiveVersionForKey("h10");
        });

        LOGGER.log(Level.INFO, "Activate entry");
        service.updateVersionStatus(versionId, ModelVersion.Status.ACTIVE);

        version = service.findActiveVersionForKey("h10");
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

        version = service.findLatestVersionForKey("h10");
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

    }

    @Test
    @Transactional
    public void testCreateAndReadRecord() throws TokenExpiredException, InvalidConsentException, InvalidTokenException, ConsentServiceException, EntityAlreadyExistsException, EntityNotFoundException, ConsentManagerException, InvalidStatusException {

        LOGGER.log(Level.INFO, "Creating, updating and activating entries");
        ModelEntry eh1 = service.createEntry("h1", "header1", "Description de header1", Header.TYPE);
        assertNotNull(eh1);
        ModelVersion v1h1 = service.createVersion(eh1.id, "fr_FR", new Header().withTitle("Header title").withBody("Header body"));
        service.updateVersionStatus(v1h1.id, ModelVersion.Status.ACTIVE);

        ModelEntry et1 = service.createEntry("t1", "treatment1", "Description de treatment1", Treatment.TYPE);
        assertNotNull(et1);
        ModelVersion v1t1 = service.createVersion(et1.id, "fr_FR", new Treatment().withTreatmentTitle("Treatment1").withDataBody("Data1").withRetentionBody("Retention1").withUsageBody("Usage1").withPurpose(Treatment.Purpose.CONSENT_MARKETING));
        service.updateVersionStatus(v1t1.id, ModelVersion.Status.ACTIVE);

        ModelEntry et2 = service.createEntry("t2", "treatment2", "Description de treatment2", Treatment.TYPE);
        assertNotNull(et2);
        ModelVersion v1t2 = service.createVersion(et2.id, "fr_FR", new Treatment().withTreatmentTitle("Treatment2").withDataBody("Data2").withRetentionBody("Retention2").withUsageBody("Usage2").withPurpose(Treatment.Purpose.CONSENT_IMPROVED_SERVICE));
        service.updateVersionStatus(v1t2.id, ModelVersion.Status.ACTIVE);

        ModelEntry ef1 = service.createEntry("f1", "footer1", "Description de footer1", Footer.TYPE);
        assertNotNull(ef1);
        ModelVersion v1f1 = service.createVersion(ef1.id, "fr_FR", new Footer().withBody("Footer body"));
        service.updateVersionStatus(v1f1.id, ModelVersion.Status.ACTIVE);

        LOGGER.info("Listing existing entries");
        List<String> types = new ArrayList<>();
        types.add(Header.TYPE);
        types.add(Treatment.TYPE);
        types.add(Footer.TYPE);
        CollectionPage<ModelEntry> entries = service.listEntries(new ModelFilter().withTypes(types).withPage(1).withSize(5));
        assertEquals(4, entries.getTotalCount());

        LOGGER.log(Level.INFO, "Creating READ context and token");
        ConsentContext readCtx = new ConsentContext()
                .setOwner(TEST_USER)
                .setSubject("mmichu")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader("h1")
                .setElements(Arrays.asList("t1", "t2"))
                .setFooter("f1")
                .setLocale("fr_FR");
        String readToken = service.buildToken(readCtx);

        LOGGER.log(Level.INFO, "Reading consent records before submit");
        List<Record> records = service.findRecordsForContext(readCtx);
        assertEquals(0, records.size());

        LOGGER.log(Level.INFO, "First consent form");
        ConsentForm form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(0, form.getPreviousValues().size());

        LOGGER.log(Level.INFO, "Creating POST context and token");
        ConsentContext postCtx = new ConsentContext()
                .setOwner(TEST_USER)
                .setSubject("mmichu")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader("element/header/" + v1h1.serial)
                .setElements(Arrays.asList("element/treatment/" + v1t1.serial, "element/treatment/" + v1t2.serial))
                .setFooter("element/footer/" + v1f1.serial)
                .setLocale("fr_FR");
        String postToken = service.buildToken(postCtx);

        LOGGER.log(Level.INFO, "Submitting first consent (creating record)");
        Map<String, String> values = new HashMap<>();
        values.put("header", "element/header/" + v1h1.serial);
        values.put("element/treatment/" + v1t1.serial, "accepted");
        values.put("element/treatment/" + v1t2.serial, "refused");
        values.put("footer", "element/footer/" + v1f1.serial);
        service.submitConsent(postToken, values);

        LOGGER.log(Level.INFO, "Reading consent records after submit");
        records = service.findRecordsForContext(readCtx);
        assertEquals(2, records.size());
        assertEquals(1, records.stream().filter(r -> r.body.equals(v1t1.serial)).count());
        assertEquals(1, records.stream().filter(r -> r.body.equals(v1t2.serial)).count());

        LOGGER.log(Level.INFO, "Second consent form");
        form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(2, form.getPreviousValues().size());

        LOGGER.log(Level.INFO, "Create new version of T2 (minor version)");
        ModelVersion v2t2 = service.createVersion(et2.id, "fr_FR", new Treatment().withTreatmentTitle("Treatment2 v2").withDataBody("Data2.2").withRetentionBody("Retention2.2").withUsageBody("Usage2.2").withPurpose(Treatment.Purpose.CONSENT_IMPROVED_SERVICE));
        service.updateVersionType(v2t2.id, ModelVersion.Type.MINOR);
        service.updateVersionStatus(v2t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.log(Level.INFO, "Reading consent records after ew version MINOR (no effect)");
        records = service.findRecordsForContext(readCtx);
        assertEquals(2, records.size());
        assertEquals(1, records.stream().filter(r -> r.body.equals(v1t1.serial)).count());
        assertEquals(1, records.stream().filter(r -> r.body.equals(v1t2.serial)).count());

        LOGGER.log(Level.INFO, "Create new version of T2 (major version)");
        ModelVersion v3t2 = service.createVersion(et2.id, "fr_FR", new Treatment().withTreatmentTitle("Treatment2 v3").withDataBody("Data2.3").withRetentionBody("Retention2.3").withUsageBody("Usage2.3").withPurpose(Treatment.Purpose.CONSENT_IMPROVED_SERVICE));
        service.updateVersionType(v3t2.id, ModelVersion.Type.MAJOR);
        service.updateVersionStatus(v3t2.id, ModelVersion.Status.ACTIVE);

        LOGGER.log(Level.INFO, "Reading consent records after new version MAJOR (no more value for t2)");
        records = service.findRecordsForContext(readCtx);
        assertEquals(1, records.size());
        assertEquals(1, records.stream().filter(r -> r.body.equals(v1t1.serial)).count());
        assertEquals(0, records.stream().filter(r -> r.body.equals(v1t2.serial)).count());

    }

}
