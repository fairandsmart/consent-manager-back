package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.ConsentElementData;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.entity.ConsentElementVersion;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenServiceException;

import javax.xml.bind.JAXBException;
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

    ConsentElementVersion findModelVersionForSerial(String serial) throws EntityNotFoundException;

    String buildToken(ConsentContext ctx);

    /**
     * Generate a Consent Form based on the information provided in the
     *
     *
     * @param token
     * @return
     * @throws EntityNotFoundException
     */
    ConsentForm generateForm(String token) throws EntityNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException;

    Receipt submitConsent(String token, Map<String, String> values) throws InvalidConsentException, TokenServiceException, TokenExpiredException, InvalidTokenException, IllegalIdentifierException, EntityNotFoundException, ModelDataSerializationException, JAXBException;

}
