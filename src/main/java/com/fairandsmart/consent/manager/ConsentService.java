package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.data.ConsentElementData;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.entity.ConsentElementVersion;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import com.fairandsmart.consent.manager.receipt.ConsentReceipt;

import java.util.Map;

public interface ConsentService {

    CollectionPage<ConsentElementEntry> listModelEntries(ModelEntryFilter filter) throws AccessDeniedException;

    String createModelEntry(String key, String name, String description, String locale, ConsentElementData content) throws ConsentManagerException, EntityAlreadyExistsException;

    ConsentElementEntry getModelEntry(String id) throws EntityNotFoundException;

    ConsentElementEntry findModelEntryByKey(String key) throws EntityNotFoundException;

    //List<ModelVersion> listModelEntryVersions(String key) throws EntityNotFoundException;

    ConsentElementVersion findActiveModelVersionForKey(String key) throws EntityNotFoundException;

    //TODO updateModelEntry()
    //TODO addModelVersion()
    //TODO deleteModelVersion()

    ConsentForm generateForm(ConsentContext ctx) throws EntityNotFoundException;

    ConsentReceipt submitConsent(ConsentContext ctx, Map<String, String> values) throws InvalidConsentException;

}
