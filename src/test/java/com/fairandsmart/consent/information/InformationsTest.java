package com.fairandsmart.consent.information;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Content;
import com.fairandsmart.consent.manager.entity.Information;
import com.fairandsmart.consent.manager.filter.InformationFilter;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class InformationsTest {

    @Inject
    ConsentService service;

    @Test
    public void testCreateInformation() throws ConsentManagerException, EntityNotFoundException {
        CollectionPage<Information> infos = service.listInformations(new InformationFilter().withType(Information.Type.HEAD).withPage(1).withSize(5));
        assertEquals(0, infos.getTotalCount());
        infos = service.listInformations(new InformationFilter().withType(Information.Type.FOOT).withPage(1).withSize(5));
        assertEquals(0, infos.getTotalCount());

        String idInfo = service.createInformation(Information.Type.HEAD, "info1", "Description de info1", "fr", "FR", new Content().withTitle("Title info 1").withBody("Body info 1").withFooter("Footer info 1"));
        assertNotNull(idInfo);

        infos = service.listInformations(new InformationFilter().withType(Information.Type.HEAD).withPage(1).withSize(5));
        assertEquals(1, infos.getTotalCount());
        infos = service.listInformations(new InformationFilter().withType(Information.Type.FOOT).withPage(1).withSize(5));
        assertEquals(0, infos.getTotalCount());

        //TODO According to the DRAFT status of info, the lookup by name should throw an exception
        Information information = service.findInformationByName("info1");
        assertNotNull(information.id);
        assertNotNull(information.serial);
        assertNotNull(information.creationDate);
        assertNotNull(information.modificationDate);
        assertEquals("info1", information.name);
        assertEquals("Description de info1", information.description);
        assertEquals("fr", information.defaultLanguage);
        assertEquals("FR", information.country);
        assertEquals("Title info 1", information.content.get(information.defaultLanguage).title);
        assertEquals("Body info 1", information.content.get(information.defaultLanguage).body);
        assertEquals("Footer info 1", information.content.get(information.defaultLanguage).footer);

    }

}
