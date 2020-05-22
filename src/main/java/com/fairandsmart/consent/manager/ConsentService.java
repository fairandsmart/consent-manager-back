package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ConsentElementData;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.entity.ConsentElementVersion;
import com.fairandsmart.consent.manager.filter.EntryFilter;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ConsentService {

    CollectionPage<ConsentElementEntry> listEntries(EntryFilter filter) throws AccessDeniedException;

    UUID createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException;

    ConsentElementEntry getEntry(UUID id) throws EntityNotFoundException, AccessDeniedException;

    ConsentElementEntry findEntryByKey(String key) throws EntityNotFoundException;

    ConsentElementVersion findActiveVersionByKey(String key) throws EntityNotFoundException;

    List<ConsentElementVersion> listVersionsForEntry(UUID id) throws ConsentManagerException;

    ConsentElementVersion getVersionBySerial(String serial) throws EntityNotFoundException;

    ConsentElementEntry updateEntry(UUID id, String name, String description) throws EntityNotFoundException, AccessDeniedException;

    void updateEntryContent(UUID id, String locale, ConsentElementData data) throws ConsentManagerException, EntityNotFoundException;

    void activateEntry(UUID id, ConsentElementVersion.Revocation revocation) throws ConsentManagerException, EntityNotFoundException;

    void archiveEntry(UUID id, ConsentElementVersion.Revocation revocation) throws ConsentManagerException, EntityNotFoundException;

    void deleteEntry(UUID id) throws ConsentManagerException, EntityNotFoundException;

    String buildToken(ConsentContext ctx);

    ConsentForm generateForm(String token) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException;

    Receipt submitConsent(String token, Map<String, String> values) throws InvalidConsentException, TokenExpiredException, InvalidTokenException, ConsentServiceException;

}
