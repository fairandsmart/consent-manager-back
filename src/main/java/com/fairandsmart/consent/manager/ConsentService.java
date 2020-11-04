package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import com.fairandsmart.consent.manager.render.RenderingException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenServiceException;

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

    Map<String, Record> systemListContextValidRecords(ConsentContext ctx) throws AccessDeniedException;

    /* Subjects */

    List<String> findSubjects(String subject) throws AccessDeniedException;

    /* Receipts */

    Receipt getReceipt(String token, String id) throws ReceiptNotFoundException, ConsentManagerException, TokenServiceException, TokenExpiredException, InvalidTokenException;

    byte[] renderReceipt(String token, String id, String format) throws ReceiptNotFoundException, ConsentManagerException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException;
}
