package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.data.ModelData;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;

import java.util.List;

public interface ConsentService {

    CollectionPage<ModelEntry> listModelEntries(ModelEntryFilter filter) throws AccessDeniedException;

    String createModelEntry(ModelEntry.Type type, String key, String name, String description, String locale, ModelData content) throws ConsentManagerException, EntityAlreadyExistsException;

    ModelEntry getModelEntry(String id) throws EntityNotFoundException;

    ModelEntry findModelEntryByKey(String key) throws EntityNotFoundException;

    //List<ModelVersion> listModelEntryVersions(String key) throws EntityNotFoundException;

    ModelVersion findActiveModelVersionForKey(String key) throws EntityNotFoundException;

    //TODO updateModelEntry()
    //TODO addModelVersion()
    //TODO deleteModelVersion()

}
