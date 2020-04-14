package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.Content;
import com.fairandsmart.consent.manager.entity.Information;
import com.fairandsmart.consent.manager.entity.ModelPart;
import com.fairandsmart.consent.manager.entity.Treatment;
import com.fairandsmart.consent.manager.filter.InformationFilter;
import com.fairandsmart.consent.manager.filter.TreatmentFilter;
import com.fairandsmart.consent.manager.repository.InformationRepository;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        PanacheQuery<Information> query = Information.find("type", filter.getType());
        CollectionPage<Information> result = new CollectionPage<>();
        result.setValues(query.page(Page.of(filter.getPage() - 1, filter.getSize())).list());
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(query.count());
        return result;
    }

    @Override
    @Transactional
    public String createInformation(Information.Type type, String name, String description, String defaultLanguage, String country, Content content) throws ConsentManagerException {
        LOGGER.log(Level.FINE, "Creating new information model");
        //TODO Include the ability to create a child (same type, and parent exists)
        try {
            Information information = new Information();
            information.type = type;
            information.name = name;
            information.description = description;
            information.defaultLanguage = defaultLanguage;
            information.country = country;
            information.content.put(defaultLanguage, content);
            information.creationDate = System.currentTimeMillis();
            information.modificationDate = information.creationDate;
            information.invalidation = ModelPart.InvalidationStrategy.OUTDATED;
            information.status = ModelPart.Status.DRAFT;
            information.serial = generator.next(information.getClass().getName());
            information.persist();
            return information.id.toString();
        } catch (SerialGeneratorException ex) {
            throw new ConsentManagerException("unable to generate serial number for information model");
        }
    }

    @Override
    public Information getInformation(String id) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Loading information model for id: " + id);
        Information information = Information.findById(UUID.fromString(id));
        if (information == null) {
            throw new EntityNotFoundException("Unable to find Information for id: " + id);
        }
        return information;
    }

    @Override
    public Information findInformationByName(String name) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Looking information model for name: " + name);
        List<Information> informations = Information.find("name", name).list();
        //TODO Apply the algorithm to determine active instance for that name
        if (informations.size() <= 0) {
            throw new EntityNotFoundException("Unable to find Information for name: " + name);
        }
        return informations.get(0);
    }

    @Override
    public CollectionPage<Treatment> listTreatments(TreatmentFilter filter) {
        LOGGER.log(Level.FINE, "Listing treatments models");
        PanacheQuery<Treatment> query = Information.findAll();
        CollectionPage<Treatment> result = new CollectionPage<>();
        result.setValues(query.page(Page.of(filter.getPage() - 1, filter.getSize())).list());
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(query.count());
        return result;
    }

    @Override
    @Transactional
    public String createTreatment(String key, String name, String description, String defaultLanguage, String country) throws ConsentManagerException {
        LOGGER.log(Level.FINE, "Creating new treatment model");
        //TODO Include the ability to create a child (same type, and parent exists)
        try {
            Treatment treatment = new Treatment();
            treatment.key = key;
            treatment.name = name;
            treatment.description = description;
            treatment.defaultLanguage = defaultLanguage;
            treatment.country = country;
            treatment.creationDate = System.currentTimeMillis();
            treatment.modificationDate = treatment.creationDate;
            treatment.invalidation = ModelPart.InvalidationStrategy.OUTDATED;
            treatment.status = ModelPart.Status.DRAFT;
            treatment.serial = generator.next(treatment.getClass().getName());
            treatment.persist();
            return treatment.id.toString();
        } catch (SerialGeneratorException ex) {
            throw new ConsentManagerException("unable to generate serial number for treatment model");
        }
    }

    @Override
    public Treatment getTreatment(String id) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Loading treatment model for id: " + id);
        Treatment treatment = Treatment.findById(UUID.fromString(id));
        if (treatment == null) {
            throw new EntityNotFoundException("Unable to find treatment for id: " + id);
        }
        return treatment;
    }

    @Override
    public Treatment findTreatmentByName(String name) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Looking treatment model for name: " + name);
        List<Treatment> treatments = Treatment.find("name", name).list();
        //TODO Apply the algorithm to determine active instance for that name
        if (treatments.size() <= 0) {
            throw new EntityNotFoundException("Unable to find Treatment for name: " + name);
        }
        return treatments.get(0);
    }

}
