package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.entity.ConsentElementVersion;
import com.fairandsmart.consent.manager.entity.ConsentRecord;
import com.fairandsmart.consent.manager.filter.EntryFilter;
import com.fairandsmart.consent.manager.model.Controller;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Treatment;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenServiceException;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ConsentServiceTest {

    private static final Logger LOGGER = Logger.getLogger(ConsentServiceTest.class.getName());

    @ConfigProperty(name = "consent.security.auth.unauthenticated")
    String unauthentifiedUser;

    @Inject
    ConsentService service;

    @Test
    public void testCreateEntryForExistingKey() throws EntityAlreadyExistsException {
        String id = service.createEntry("existing", "header1", "description", "header");
        assertNotNull(id);
        assertThrows(EntityAlreadyExistsException.class, () -> {
            service.createEntry("existing", "header1", "description", "header");
        });
    }

    @Test
    @Transactional
    public void testCreateAndUpdateHeaderEntry() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.info("List existing entries for headers");
        CollectionPage<ConsentElementEntry> headers = service.listEntries(new EntryFilter().withOwner(unauthentifiedUser).withTypes(Collections.singletonList(Header.TYPE)).withPage(1).withSize(5));
        long headersCountBeforeCreate = headers.getTotalCount();

        String id = service.createEntry("e1", "entry1", "Description de entry1", Header.TYPE);
        assertNotNull(id);

        LOGGER.info("List existing entries for headers");
        headers = service.listEntries(new EntryFilter().withTypes(Collections.singletonList(Header.TYPE)).withOwner(unauthentifiedUser).withPage(1).withSize(5));
        assertEquals(headersCountBeforeCreate + 1, headers.getTotalCount());

        LOGGER.info("Lookup entry e1 by key");
        ConsentElementEntry entry = service.findEntryByKey("e1");
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertNotNull(entry.id);
        assertNotNull(entry.owner);
        assertEquals(Header.TYPE, entry.type);
        assertEquals("e1", entry.key);
        assertEquals("entry1", entry.name);
        assertEquals("Description de entry1", entry.description);

        LOGGER.info("Update entry with key e1");
        service.updateEntry(id, "Entry Name Updated", "Entry Description Updated");
        entry = service.findEntryByKey("e1");
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertEquals("Entry Name Updated", entry.name);
        assertEquals("Entry Description Updated", entry.description);

        String fakeid = UUID.randomUUID().toString();
        assertThrows(EntityNotFoundException.class, () -> {
            service.updateEntry(fakeid, "Entry Name Updated", "Entry Description Updated");
        });

        LOGGER.info("Check no version exists");
        List<ConsentElementVersion> versions = service.listVersionsForEntry(id);
        assertEquals(0, versions.size());
    }

    @Test
    @Transactional
    public void testCreateAndUpdateHeaderContent() throws ConsentManagerException, EntityAlreadyExistsException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.info("Create entry");
        String id = service.createEntry("h1", "header1", "Description de header1", Header.TYPE);
        assertNotNull(id);

        LOGGER.info("List versions");
        List<ConsentElementVersion> versions = service.listVersionsForEntry(id);
        assertEquals(0, versions.size());

        LOGGER.info("Create Header h1");
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
        service.updateEntryContent(id, "fr_FR", header);

        LOGGER.info("List versions");
        versions = service.listVersionsForEntry(id);
        assertEquals(1, versions.size());
        assertEquals("fr_FR", versions.get(0).availableLocales);
        assertEquals("fr_FR", versions.get(0).defaultLocale);
        assertEquals(ConsentElementVersion.Status.DRAFT, versions.get(0).status);
        assertEquals(unauthentifiedUser, versions.get(0).owner);
        assertNotNull(versions.get(0).serial);
        assertEquals(unauthentifiedUser, versions.get(0).content.get("fr_FR").author);
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

        ConsentElementVersion version = service.getVersionBySerial(versions.get(0).serial);
        assertEquals(versions.get(0), version);

        //At this point no version is active
        assertThrows(EntityNotFoundException.class, () -> {
            service.findActiveVersionByKey("h1");
        });

        LOGGER.log(Level.INFO, "Activate entry");
        service.activateEntry(id, ConsentElementVersion.Revocation.SUPPORTS);

        version = service.findActiveVersionByKey("h1");
        assertEquals(versions.get(0), version);
        assertEquals(ConsentElementVersion.Status.ACTIVE, version.status);
    }

    @Test
    @Transactional
    public void testCreateAndReadRecord() throws TokenExpiredException, InvalidConsentException, InvalidTokenException, ConsentServiceException, EntityAlreadyExistsException, EntityNotFoundException, ConsentManagerException, TokenServiceException, IllegalIdentifierException {
        LOGGER.log(Level.INFO, "Creating, updating and activating entries");
        String id = service.createEntry("h1", "header1", "Description de header1", Header.TYPE);
        assertNotNull(id);
        String headerSerial = service.updateEntryContent(id, "fr_FR", new Header().withTitle("Header title").withBody("Header body")).serial;
        service.activateEntry(id, ConsentElementVersion.Revocation.SUPPORTS);
        id = service.createEntry("t1", "treatment1", "Description de treatment1", Treatment.TYPE);
        assertNotNull(id);
        String treatment1Serial = service.updateEntryContent(id, "fr_FR", new Treatment().withTreatmentTitle("Treatment1").withDataBody("Data1").withRetentionBody("Retention1").withUsageBody("Usage1").withPurpose(Treatment.Purpose.CONSENT_MARKETING)).serial;
        service.activateEntry(id, ConsentElementVersion.Revocation.SUPPORTS);
        id = service.createEntry("t2", "treatment2", "Description de treatment2", Treatment.TYPE);
        assertNotNull(id);
        String treatment2Serial = service.updateEntryContent(id, "fr_FR", new Treatment().withTreatmentTitle("Treatment2").withDataBody("Data2").withRetentionBody("Retention2").withUsageBody("Usage2").withPurpose(Treatment.Purpose.CONSENT_IMPROVED_SERVICE)).serial;
        service.activateEntry(id, ConsentElementVersion.Revocation.SUPPORTS);
        id = service.createEntry("f1", "footer1", "Description de footer1", Footer.TYPE);
        assertNotNull(id);
        String footerSerial = service.updateEntryContent(id, "fr_FR", new Footer().withBody("Footer body")).serial;
        service.activateEntry(id, ConsentElementVersion.Revocation.SUPPORTS);

        LOGGER.info("Listing existing entries");
        List<String> types = new ArrayList<>();
        types.add(Header.TYPE);
        types.add(Treatment.TYPE);
        types.add(Footer.TYPE);
        CollectionPage<ConsentElementEntry> entries = service.listEntries(new EntryFilter().withTypes(types).withPage(1).withSize(5));
        assertEquals(4, entries.getTotalCount());

        LOGGER.log(Level.INFO, "Creating READ context and token");
        ConsentContext readCtx = new ConsentContext()
                .setOwner(unauthentifiedUser)
                .setSubject("mmichu")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader("h1")
                .setElements(Arrays.asList("t1", "t2"))
                .setFooter("f1")
                .setLocale("fr_FR");
        String readToken = service.buildToken(readCtx); // TODO : actuellement, il faut changer la valeur de consent.security.auth.unauthenticated en sheldon ; cf le TODO de buildToken

        LOGGER.log(Level.INFO, "Reading consent records before submit");
        List<ConsentRecord> records = service.listConsentRecords(readCtx);
        assertEquals(0, records.size());

        LOGGER.log(Level.INFO, "First consent form");
        ConsentForm form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(0, form.getPreviousValues().size());

        LOGGER.log(Level.INFO, "Creating POST context and token");
        ConsentContext postCtx = new ConsentContext()
                .setOwner(unauthentifiedUser)
                .setSubject("mmichu")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader("element/header/" + headerSerial)
                .setElements(Arrays.asList("element/treatment/" + treatment1Serial, "element/treatment/" + treatment2Serial))
                .setFooter("element/footer/" + footerSerial)
                .setLocale("fr_FR");
        String postToken = service.buildToken(postCtx); // TODO : actuellement, il faut changer la valeur de consent.security.auth.unauthenticated en sheldon ; cf le TODO de buildToken

        LOGGER.log(Level.INFO, "Submitting first consent (creating record)");
        Map<String, String> values = new HashMap<>();
        values.put("header", "element/header/" + headerSerial);
        values.put("element/treatment/" + treatment1Serial, "accepted");
        values.put("element/treatment/" + treatment2Serial, "refused");
        values.put("footer", "element/footer/" + footerSerial);
        service.submitConsent(postToken, values);

        LOGGER.log(Level.INFO, "Reading consent records after submit");
        records = service.listConsentRecords(readCtx);
        assertEquals(2, records.size());
        assertEquals(1, records.stream().filter(r -> r.body.equals(treatment1Serial)).count());
        assertEquals(1, records.stream().filter(r -> r.body.equals(treatment2Serial)).count());

        LOGGER.log(Level.INFO, "Second consent form");
        form = service.generateForm(readToken);
        assertEquals(2, form.getElements().size());
        assertEquals(2, form.getPreviousValues().size());
    }

}
