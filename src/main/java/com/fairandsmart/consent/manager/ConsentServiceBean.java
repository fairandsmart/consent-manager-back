package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.data.ModelData;
import com.fairandsmart.consent.manager.entity.ModelContent;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.serial.SerialGenerator;
import com.fairandsmart.consent.serial.SerialGeneratorException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.fairandsmart.consent.manager.entity.ModelEntry.DEFAULT_BRANCHE;

@ApplicationScoped
public class ConsentServiceBean implements ConsentService {

    private static final Logger LOGGER = Logger.getLogger(ConsentService.class.getName());

    @Inject
    AuthenticationService authentication;

    @Inject
    SerialGenerator generator;

    @Override
    public CollectionPage<ModelEntry> listModelEntries(ModelEntryFilter filter) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Listing models entries");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        if ( !connectedIdentifier.equals(filter.getOwner()) ) {
            throw new AccessDeniedException("Owner must be the connected user");
        }
        PanacheQuery<ModelEntry> query = ModelEntry.find("owner = ?1 and type = ?2", filter.getOwner(), filter.getType());
        CollectionPage<ModelEntry> result = new CollectionPage<>();
        result.setValues(query.page(Page.of(filter.getPage()-1, filter.getSize())).list());
        result.setPageSize(filter.getSize());
        result.setPage(filter.getPage());
        result.setTotalPages(query.pageCount());
        result.setTotalCount(query.count());
        return result;
    }

    @Override
    @Transactional
    public String createModelEntry(ModelEntry.Type type, String key, String name, String description, String locale, ModelData data) throws ConsentManagerException, EntityAlreadyExistsException {
        LOGGER.log(Level.FINE, "Creating new model entry");
        String connectedIdentifier = authentication.getConnectedIdentifier();
        if ( ModelEntry.isKeyAlreadyExistsForOwner(connectedIdentifier, key)) {
            throw new EntityAlreadyExistsException("A model entry already exists with key: " + key);
        }
        try {
            ModelEntry entry = new ModelEntry();
            entry.type = type;
            entry.key = key;
            entry.name = name;
            entry.description = description;
            entry.branches = DEFAULT_BRANCHE;
            entry.owner = connectedIdentifier;
            entry.persist();

            ModelVersion version = new ModelVersion();
            version.entry = entry;
            version.author = connectedIdentifier;
            version.owner = connectedIdentifier;
            version.branches = DEFAULT_BRANCHE;
            version.creationDate = System.currentTimeMillis();
            version.modificationDate = version.creationDate;
            version.invalidation = ModelVersion.Invalidation.INVALIDATE;
            version.status = ModelVersion.Status.DRAFT;
            version.serial = generator.next(version.getClass().getName());
            version.defaultLocale = locale;
            version.availableLocales = locale;
            version.content.put(locale, new ModelContent().withModelData(data));
            version.persist();

            return entry.id.toString();
        } catch ( SerialGeneratorException ex ) {
            throw new ConsentManagerException("unable to generate serial number for information model");
        } catch ( ModelDataSerializationException ex ) {
            throw new ConsentManagerException("unable to serialise model data");
        }
    }

    @Override
    public ModelEntry getModelEntry(String id) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Getting model entry for id: " + id);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        ModelEntry entry = ModelEntry.findById(UUID.fromString(id));
        if ( entry == null || !entry.owner.equals(connectedIdentifier) ) {
            throw new EntityNotFoundException("Unable to find an entry for id: " + id);
        }
        return entry;
    }

    @Override
    public ModelEntry findModelEntryByKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding model entry for key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        List<ModelEntry> entries = ModelEntry.find("owner = ?1 and key = ?2", connectedIdentifier, key).list();
        if ( entries.isEmpty() ) {
            throw new EntityNotFoundException("Unable to find an entry for key: " + key);
        }
        return entries.get(0);
    }

    @Override
    public ModelVersion findActiveModelVersionForKey(String key) throws EntityNotFoundException {
        LOGGER.log(Level.FINE, "Finding active model version for key: " + key);
        String connectedIdentifier = authentication.getConnectedIdentifier();
        //TODO for now we only works with DRAFT
        List<ModelVersion> versions = ModelVersion.find("owner = ?1 and entry.key = ?2 and status = ?3", connectedIdentifier, key, ModelVersion.Status.DRAFT).list();
        if ( versions.isEmpty() ) {
            throw new EntityNotFoundException("Unable to find an entry for key: " + key);
        }
        if ( versions.size() > 1 ) {
            LOGGER.log(Level.WARNING, "Found more than one active version, this is an incoherency");
        }
        return versions.get(0);
    }
}
