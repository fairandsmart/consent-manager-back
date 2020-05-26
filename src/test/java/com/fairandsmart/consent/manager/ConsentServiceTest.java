package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.model.Controller;
import com.fairandsmart.consent.manager.model.Header;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
    public void testCreateAndUpdateHeaderContent() throws ConsentManagerException, EntityAlreadyExistsException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.info("Create entry");
        String entryId = service.createEntry("h1", "header1", "Description de header1", Header.TYPE).id;
        assertNotNull(entryId);

        LOGGER.info("List versions");
        List<ModelVersion> versions = service.getVersionHistoryForKey(entryId);
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
        String versionId = service.createVersion(entryId, "fr_FR", header).id;

        LOGGER.info("List versions");
        versions = service.getVersionHistoryForEntry(entryId);
        assertEquals(1, versions.size());
        assertEquals("fr_FR", versions.get(0).availableLocales);
        assertEquals("fr_FR", versions.get(0).defaultLocale);
        assertEquals(ModelVersion.Status.DRAFT, versions.get(0).status);
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

        ModelVersion version = service.findVersionForSerial(versions.get(0).serial);
        assertEquals(versions.get(0), version);

        //At this point no version is active
        assertThrows(EntityNotFoundException.class, () -> {
            service.findActiveVersionForKey("h1");
        });

        LOGGER.log(Level.INFO, "Activate entry");
        service.updateVersionStatus(versionId, ModelVersion.Status.ACTIVE);

        version = service.findActiveVersionForKey("h1");
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

        version = service.findLatestVersionForKey("h1");
        assertEquals(versions.get(0), version);
        assertEquals(ModelVersion.Status.ACTIVE, version.status);

    }

}
