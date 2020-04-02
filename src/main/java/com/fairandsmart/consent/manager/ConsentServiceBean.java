package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.Content;
import com.fairandsmart.consent.manager.entity.Information;
import com.fairandsmart.consent.manager.filter.InformationFilter;
import com.fairandsmart.consent.manager.repository.InformationRepository;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ConsentServiceBean implements ConsentService {

    private static final Logger LOGGER = Logger.getLogger(ConsentService.class.getName());

    @Inject
    InformationRepository repository;

    @Inject
    SerialGenerator generator;

    @Override
    public CollectionPage<Information> listInformations(InformationFilter filter) {
        LOGGER.log(Level.FINE, "Listing informations models");
        PanacheQuery<Information> query = repository.find("type", filter.getType());
        query.page(Page.of(filter.getPage(), filter.getSize()));
        CollectionPage<Information> result = new CollectionPage<>();
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(repository.count());
        result.setValues(query.list());
        return result;
    }

    @Override
    @Transactional
    public String createInformation(Information.Type type, String name, String description, String defaultLanguage, String country, Content content) throws ConsentManagerException {
        LOGGER.log(Level.FINE, "Creating new information model");
        //TODO Include the ability to create a child (same type, and parent exists)
        try {
            Information information = new Information();
            information.id = UUID.randomUUID().toString();
            information.type = type;
            information.name = name;
            information.description = description;
            information.defaultLanguage = defaultLanguage;
            information.country = country;
            information.content.put(defaultLanguage, content);
            information.creationDate = System.currentTimeMillis();
            information.modificationDate = information.creationDate;
            information.serial = generator.next(information.getClass().getName());
            return information.id;
        } catch ( SerialGeneratorException ex ) {
            throw new ConsentManagerException("unable to generate serial number for information models");
        }
    }

    @Override
    public Information getInformation(String id) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Loading information model for id: " + id);
        Information information = Information.findById(id);
        if (information == null) {
            throw new EntityNotFoundException("Unable to find Information for id: " + id);
        }
        return information;
    }
}
