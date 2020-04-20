package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.data.Information;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
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
    public void testCreateModelEntryForExistingKey() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        Information info = new Information();
        info.setTitle("title");
        info.setBody("body");
        info.setFooter("footer");

        String idInfo = service.createModelEntry(ModelEntry.Type.HEADER, "existing", "info1", "description", "fr-FR", info);
        assertNotNull(idInfo);
        try {
            service.createModelEntry(ModelEntry.Type.HEADER, "existing", "info1", "description", "fr-FR", info);
            fail("The key should already be used");
        } catch ( EntityAlreadyExistsException e ) { //ok
        }
    }

    @Test
    public void testCreateModelEntry() throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.info("List existing models for headers or footers");
        CollectionPage<ModelEntry> headers = service.listModelEntries(new ModelEntryFilter().withOwner(unauthentifiedUser).withType(ModelEntry.Type.HEADER).withPage(1).withSize(5));
        assertEquals(0, headers.getTotalCount());
        CollectionPage<ModelEntry> footers = service.listModelEntries(new ModelEntryFilter().withOwner(unauthentifiedUser).withType(ModelEntry.Type.FOOTER).withPage(1).withSize(5));
        assertEquals(0, footers.getTotalCount());

        LOGGER.info("Create Information Header h1");
        Information info = new Information();
        info.setTitle("Title info 1");
        info.setBody("Body info 1");
        info.setFooter("Footer info 1");
        String idInfo = service.createModelEntry(ModelEntry.Type.HEADER, "h1", "info1", "Description de info1", "fr-FR", info);
        assertNotNull(idInfo);

        LOGGER.info("List existing models for headers or footers");
        headers = service.listModelEntries(new ModelEntryFilter().withType(ModelEntry.Type.HEADER).withOwner(unauthentifiedUser).withPage(1).withSize(5));
        assertEquals(1, headers.getTotalCount());
        footers = service.listModelEntries(new ModelEntryFilter().withType(ModelEntry.Type.FOOTER).withOwner(unauthentifiedUser).withPage(1).withSize(5));
        assertEquals(0, footers.getTotalCount());

        LOGGER.info("Lookup model h1 by key");
        ModelEntry entry = service.findModelEntryByKey("h1");
        assertNotNull(entry.id);
        assertNotNull(entry.owner);
        assertEquals(ModelEntry.Type.HEADER, entry.type);
        assertEquals("h1", entry.key);
        assertEquals("info1", entry.name);
        assertEquals("Description de info1", entry.description);

        //TODO List version of that entry and check that the content exists in the root version
        //assertEquals("FR", entry.);
        //assertEquals("Title info 1", information.content.get(information.defaultLanguage).title);
        //assertEquals("Body info 1", information.content.get(information.defaultLanguage).body);
        //assertEquals("Footer info 1", information.content.get(information.defaultLanguage).footer);

    }

}