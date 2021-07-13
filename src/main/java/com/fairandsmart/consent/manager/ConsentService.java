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
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.exception.*;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import com.fairandsmart.consent.manager.render.RenderingException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ConsentService {

    /* Entries */

    CollectionPage<ModelEntry> listEntries(ModelFilter filter);

    ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException, UnexpectedException, AccessDeniedException;

    ModelEntry getEntry(String entryId) throws EntityNotFoundException, AccessDeniedException;

    ModelEntry findEntryForKey(String key) throws EntityNotFoundException;

    ModelEntry updateEntry(String entryId, String name, String description) throws EntityNotFoundException, AccessDeniedException;

    void deleteEntry(String entryId) throws UnexpectedException, EntityNotFoundException, InvalidStatusException, AccessDeniedException;

    /* Versions */

    ModelVersion createVersion(String entryId, String defaultLanguage, Map<String, ModelData> data) throws UnexpectedException, EntityNotFoundException, AccessDeniedException;

    ModelVersion findActiveVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findActiveVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findLatestVersionForKey(String key) throws EntityNotFoundException;

    ModelVersion findLatestVersionForEntry(String entryId) throws EntityNotFoundException;

    ModelVersion findVersionForSerial(String serial) throws EntityNotFoundException;

    ModelVersion getVersion(String versionId) throws EntityNotFoundException, AccessDeniedException;

    List<ModelVersion> getVersionHistoryForKey(String key) throws UnexpectedException;

    List<ModelVersion> getVersionHistoryForEntry(String entryId) throws UnexpectedException;

    ModelVersion updateVersion(String versionId, String defaultLanguage, Map<String, ModelData> data) throws UnexpectedException, EntityNotFoundException, AccessDeniedException;

    ModelVersion updateVersionType(String versionId, ModelVersion.Type type) throws UnexpectedException, EntityNotFoundException, AccessDeniedException;

    ModelVersion updateVersionStatus(String versionId, ModelVersion.Status status) throws UnexpectedException, EntityNotFoundException, InvalidStatusException, AccessDeniedException;

    PreviewDto previewVersion(String entryId, String versionId, PreviewDto dto) throws AccessDeniedException, EntityNotFoundException, ModelDataSerializationException;

    void deleteVersion(String versionId) throws UnexpectedException, EntityNotFoundException, AccessDeniedException;

    /* Transactions */

    Transaction createTransaction(ConsentContext ctx) throws AccessDeniedException, ConsentContextSerializationException;

    Transaction getTransaction(String txId) throws AccessDeniedException, EntityNotFoundException;

    boolean isTransactionExists(String txId);

    List<Transaction> listTransactions() throws AccessDeniedException;

    long countTransactions(long from, long to) throws AccessDeniedException;

    ConsentSubmitForm getConsentForm(String txId) throws GenerateFormException, UnexpectedException, AccessDeniedException, EntityNotFoundException;

    void submitConsentValues(String txId, MultivaluedMap<String, String> values) throws UnexpectedException, SubmitConsentException, AccessDeniedException, EntityNotFoundException;

    ConsentConfirmForm getConfirmationForm(String txId) throws UnexpectedException, GenerateFormException, AccessDeniedException, EntityNotFoundException;

    void submitConfirmationValues(String txId, MultivaluedMap<String, String> values) throws UnexpectedException, SubmitConsentException, AccessDeniedException, EntityNotFoundException, ConfirmationException;

    Transaction breedTransaction(String txId) throws AccessDeniedException, ConsentContextSerializationException, EntityNotFoundException;

    /* Preview */

    ConsentSubmitForm getConsentFormPreview(ConsentContext ctx) throws GenerateFormException, UnexpectedException, AccessDeniedException, EntityNotFoundException;

    /* Records */

    Map<String, List<Record>> listSubjectRecords(RecordFilter filter) throws AccessDeniedException;

    Map<String, Record> systemListValidRecords(String subject, String infoKey, List<String> elementsKeys) throws AccessDeniedException;

    Map<Subject, Record> extractRecords(String key, String value, boolean regexpValue) throws AccessDeniedException, EntityNotFoundException;

    /* Subjects */

    List<Subject> findSubjects(String subject) throws AccessDeniedException;

    Subject getSubject(String name) throws AccessDeniedException;

    Subject createSubject(String name, String email) throws UnexpectedException, EntityAlreadyExistsException, AccessDeniedException;

    Subject updateSubject(String id, String email) throws AccessDeniedException, EntityNotFoundException;

    /* Receipts */

    ConsentReceipt getReceipt(String id) throws ReceiptNotFoundException, UnexpectedException, AccessDeniedException;

    byte[] renderReceipt(String id, String format, String themeKey) throws ReceiptNotFoundException, UnexpectedException, ReceiptRendererNotFoundException, RenderingException, EntityNotFoundException, ModelDataSerializationException, AccessDeniedException;

    byte[] systemRenderReceipt(String id, String format, String themeKey) throws ReceiptRendererNotFoundException, ReceiptNotFoundException, UnexpectedException, IOException, JAXBException, RenderingException, ModelDataSerializationException, EntityNotFoundException;
}
