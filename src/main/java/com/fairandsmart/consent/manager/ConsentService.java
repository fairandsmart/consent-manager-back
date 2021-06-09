package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.exception.*;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import com.fairandsmart.consent.manager.render.RenderingException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.manager.store.ReceiptStoreException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenServiceException;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ConsentService {

    /* Entries */

    CollectionPage<ModelEntry> listEntries(ModelFilter filter);

    ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException, ConsentManagerException;

    ModelEntry getEntry(String entryId) throws EntityNotFoundException, AccessDeniedException;

    ModelEntry findEntryForKey(String key) throws EntityNotFoundException;

    ModelEntry updateEntry(String entryId, String name, String description) throws EntityNotFoundException, AccessDeniedException;

    void deleteEntry(String entryId) throws ConsentManagerException, EntityNotFoundException, InvalidStatusException;

    /* Versions */

    ModelVersion createVersion(String entryId, String defaultLanguage, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException;

    ModelVersion getVersion(String versionId) throws EntityNotFoundException, AccessDeniedException;

    List<ModelVersion> getVersionHistoryForKey(String key) throws ConsentManagerException;

    List<ModelVersion> getVersionHistoryForEntry(String entryId) throws ConsentManagerException;

    ModelVersion updateVersion(String versionId, String defaultLanguage, Map<String, ModelData> data) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion updateVersionType(String versionId, ModelVersion.Type type) throws ConsentManagerException, EntityNotFoundException;

    ModelVersion updateVersionStatus(String versionId, ModelVersion.Status status) throws ConsentManagerException, EntityNotFoundException, InvalidStatusException;

    PreviewDto previewVersion(String entryId, String versionId, PreviewDto dto) throws AccessDeniedException, EntityNotFoundException, ModelDataSerializationException;

    void deleteVersion(String versionId) throws ConsentManagerException, EntityNotFoundException;

    /* Forms */

    String buildFormToken(ConsentContext ctx) throws AccessDeniedException;

    ConsentForm generateForm(String token) throws TokenExpiredException, InvalidTokenException, ConsentServiceException, GenerateFormException;

    ConsentReceipt submitConsent(String token, MultivaluedMap<String, String> values) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, SubmitConsentException;

    /* Records */

    Map<String, List<Record>> listSubjectRecords(RecordFilter filter) throws AccessDeniedException;

    Map<String, Record> systemListValidRecords(String subject, String infoKey, List<String> elementsKeys) throws AccessDeniedException;

    Map<Subject, Record> extractRecords(String key, String value, boolean regexpValue) throws AccessDeniedException, EntityNotFoundException;

    /* Transactions */

    Record.State getTransactionState(String transaction);

    boolean isTransactionExists(String transaction);

    long countTransactionsCreatedBetween(long from, long to);


    /* Subjects */

    List<Subject> findSubjects(String subject) throws AccessDeniedException;

    Subject getSubject(String name) throws AccessDeniedException;

    Subject createSubject(String name, String email) throws ConsentManagerException, EntityAlreadyExistsException;

    Subject updateSubject(String id, String email) throws AccessDeniedException, EntityNotFoundException;

    String buildSubjectToken(SubjectContext ctx) throws AccessDeniedException;

    /* Receipts */

    ConsentReceipt getReceipt(String id) throws ReceiptNotFoundException, ConsentManagerException;

    String buildReceiptToken(ReceiptContext ctx) throws AccessDeniedException, ReceiptStoreException, ReceiptNotFoundException;

    byte[] renderReceipt(String id, String format, String themeKey) throws ReceiptNotFoundException, ConsentManagerException, ReceiptRendererNotFoundException, RenderingException, EntityNotFoundException, ModelDataSerializationException;

    byte[] systemRenderReceipt(String id, String format, String themeKey) throws ReceiptRendererNotFoundException, ReceiptStoreException, ReceiptNotFoundException, IOException, JAXBException, RenderingException, ModelDataSerializationException, EntityNotFoundException;
}
