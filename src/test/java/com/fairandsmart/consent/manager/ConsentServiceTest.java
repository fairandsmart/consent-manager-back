package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ConsentElementData;
import com.fairandsmart.consent.manager.entity.ConsentElementVersion;
import com.fairandsmart.consent.manager.model.Controller;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
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
    public void testCreateEntryForExistingKey() throws ConsentManagerException, EntityAlreadyExistsException {
        Header header = new Header();
        header.setTitle("title");
        header.setBody("body");
        header.setPrivacyPolicyUrl("readmorelink");

        String idHeader = service.createEntry("existing", "header1", "description", "fr_FR", header);
        assertNotNull(idHeader);
        assertThrows(EntityAlreadyExistsException.class, () -> {
            service.createEntry("existing", "header1", "description", "fr_FR", header);
        });
    }

    @Test
    @Transactional
    public void testCreateAndUpdateHeaderEntry() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException, ModelDataSerializationException {
        LOGGER.info("List existing entries for headers");
        CollectionPage<ConsentElementEntry> headers = service.listEntries(new ModelEntryFilter().withOwner(unauthentifiedUser).withType(Header.TYPE).withPage(1).withSize(5));
        long headersCountBeforeCreate = headers.getTotalCount();

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
        String idHeader = service.createEntry("h1", "header1", "Description de header1", "fr_FR", header);
        assertNotNull(idHeader);

        LOGGER.info("List existing entries for headers");
        headers = service.listEntries(new ModelEntryFilter().withType(Header.TYPE).withOwner(unauthentifiedUser).withPage(1).withSize(5));
        assertEquals(headersCountBeforeCreate + 1, headers.getTotalCount());

        LOGGER.info("Lookup entry h1 by key");
        ConsentElementEntry entry = service.findEntryByKey("h1");
        assertNotNull(entry.id);
        assertNotNull(entry.owner);
        assertEquals(Header.TYPE, entry.type);
        assertEquals("h1", entry.key);
        assertEquals("header1", entry.name);
        assertEquals("Description de header1", entry.description);

        LOGGER.info("List version");
        List<ConsentElementVersion> versions = service.listVersionsForEntry("h1");
        assertEquals(1, versions.size());
        assertEquals("fr_FR", versions.get(0).availableLocales);
        assertEquals("fr_FR", versions.get(0).defaultLocale);
        assertEquals(ConsentElementVersion.Status.DRAFT, versions.get(0).status);
        assertEquals(unauthentifiedUser, versions.get(0).author);
        assertEquals(unauthentifiedUser, versions.get(0).owner);
        assertNotNull(versions.get(0).serial);

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

        //At this point no version is active
        assertThrows(EntityNotFoundException.class, () -> {
           service.findActiveVersionForEntry("h1");
        });

        ConsentElementVersion version = service.findVersionBySerial(versions.get(0).serial);
        assertNotNull(version);
        assertEquals(versions.get(0), version);

        assertThrows(EntityNotFoundException.class, () -> {
            service.updateEntry("h2", "Entry Name Updated", "Entry Description Updated");
        });

        service.updateEntry("h1", "Entry Name Updated", "Entry Description Updated");
        entry = service.findEntryByKey("h1");
        LOGGER.log(Level.INFO, "Entry: " + entry);
        assertEquals("Entry Name Updated", entry.name);
        assertEquals("Entry Description Updated", entry.description);
    }

}
