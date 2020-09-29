package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

public interface ConsentService {

    /* Entries */

    CollectionPage<ModelEntry> listEntries(ModelFilter filter);

    ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException, AccessDeniedException;

    ModelEntry getEntry(String entryId) throws EntityNotFoundException, AccessDeniedException;

    ModelEntry findEntryForKey(String key) throws EntityNotFoundException;

    ModelEntry updateEntry(String entryId, String name, String description) throws EntityNotFoundException, AccessDeniedException;

    void deleteEntry(String entryId) throws ConsentManagerException, EntityNotFoundException;

    /* Versions */

    ModelVersion createVersion(String entryId, String defaultLocale, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException;

    ModelVersion getVersion(String versionId) throws EntityNotFoundException, AccessDeniedException;

    List<ModelVersion> getVersionHistoryForKey(String key) throws ConsentManagerException;

    List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException;

    ModelVersion updateVersion(String versionId, String defaultLocale, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion updateVersionType(String versionId, ModelVersion.Type type) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion updateVersionStatus(String versionId, ModelVersion.Status status) throws ConsentManagerException, EntityNotFoundException, InvalidStatusException;

    PreviewDto previewVersion(String entryId, String versionId, PreviewDto dto) throws AccessDeniedException, EntityNotFoundException, ModelDataSerializationException;

    void deleteVersion(String versionId) throws ConsentManagerException, EntityNotFoundException;

    /* Forms */

    String buildToken(ConsentContext ctx) throws AccessDeniedException;

    ConsentForm generateForm(String token) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException;

    Receipt submitConsent(String token, MultivaluedMap<String, String> values) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, InvalidConsentException;

    /* Records */

    CollectionPage<Record> listRecords(RecordFilter filter) throws AccessDeniedException;

    List<Record> systemFindRecordsForContext(ConsentContext ctx) throws AccessDeniedException;

    Map<String, List<Record>> listSubjectRecords(String subject) throws AccessDeniedException;

    List<String> findSubjects(String subject) throws AccessDeniedException;

}
