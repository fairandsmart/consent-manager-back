package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;

import java.util.List;
import java.util.Map;

public interface ConsentService {

    CollectionPage<ModelEntry> listEntries(ModelFilter filter);

    ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException;

    ModelEntry getEntry(String entryId) throws EntityNotFoundException, AccessDeniedException;

    ModelEntry findEntryForKey(String key) throws EntityNotFoundException;

    ModelEntry updateEntry(String entryId, String name, String description) throws EntityNotFoundException, AccessDeniedException;

    void deleteEntry(String entryId) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion createVersion(String entryId, String locale, ModelData data) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException;

    ModelVersion getVersion(String versionId) throws EntityNotFoundException;

    List<ModelVersion> getVersionHistoryForKey(String key) throws ConsentManagerException;

    List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException;

    ModelVersion updateVersion(String versionId, String locale, ModelData data) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion updateVersionType(String versionId, ModelVersion.Type type) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion updateVersionStatus(String versionId, ModelVersion.Status status) throws ConsentManagerException, EntityNotFoundException;

    void deleteVersion(String versionId) throws ConsentManagerException, EntityNotFoundException;

    String buildToken(ConsentContext ctx);

    ConsentForm generateForm(String token) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException;

    Receipt submitConsent(String token, Map<String, String> values) throws InvalidConsentException, TokenExpiredException, InvalidTokenException, ConsentServiceException;

}
