package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
 * #L%
 */

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.*;
import com.fairandsmart.consent.manager.exception.ConsentServiceException;
import com.fairandsmart.consent.manager.exception.InvalidConsentException;
import com.fairandsmart.consent.manager.exception.InvalidStatusException;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.model.Receipt;
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

    ModelEntry createEntry(String key, String name, String description, String type) throws EntityAlreadyExistsException, AccessDeniedException;

    ModelEntry getEntry(String entryId) throws EntityNotFoundException, AccessDeniedException;

    ModelEntry findEntryForKey(String key) throws EntityNotFoundException;

    ModelEntry updateEntry(String entryId, String name, String description) throws EntityNotFoundException, AccessDeniedException;

    void deleteEntry(String entryId) throws ConsentManagerException, EntityNotFoundException;

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

    String buildToken(ConsentContext ctx) throws AccessDeniedException;

    ConsentForm generateForm(String token) throws EntityNotFoundException, TokenExpiredException, InvalidTokenException, ConsentServiceException;

    ConsentTransaction submitConsent(String token, MultivaluedMap<String, String> values) throws InvalidTokenException, TokenExpiredException, ConsentServiceException, InvalidConsentException;

    /* Records */

    Map<String, List<Record>> listSubjectRecords(String subject) throws AccessDeniedException;

    Map<String, Record> systemListValidRecords(String subject, String infoKey, List<String> elementsKeys) throws AccessDeniedException;

    Map<Subject, Record> extractRecords(String key, String value, boolean regexpValue) throws AccessDeniedException;

    /* Subjects */

    List<Subject> findSubjects(String subject) throws AccessDeniedException;

    Subject getSubject(String name) throws AccessDeniedException;

    Subject createSubject(String name, String email) throws ConsentManagerException, EntityAlreadyExistsException;

    Subject updateSubject(String id, String email) throws AccessDeniedException, EntityNotFoundException;

    /* Receipts */

    Receipt getReceipt(String token, String id) throws ReceiptNotFoundException, ConsentManagerException, TokenServiceException, TokenExpiredException, InvalidTokenException;

    byte[] renderReceipt(String token, String id, String format, String themeKey) throws ReceiptNotFoundException, ConsentManagerException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException, EntityNotFoundException, ModelDataSerializationException;

    byte[] systemRenderReceipt(String id, String format, String themeKey) throws ReceiptRendererNotFoundException, ReceiptStoreException, ReceiptNotFoundException, IOException, JAXBException, RenderingException, ModelDataSerializationException, EntityNotFoundException;
}
