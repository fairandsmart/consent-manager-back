package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.Content;
import com.fairandsmart.consent.manager.entity.Information;
import com.fairandsmart.consent.manager.entity.Treatment;
import com.fairandsmart.consent.manager.filter.InformationFilter;
import com.fairandsmart.consent.manager.filter.TreatmentFilter;

import javax.transaction.Transactional;

public interface ConsentService {

    CollectionPage<Information> listInformations(InformationFilter filter);

    String createInformation(Information.Type type, String name, String description, String defaultLanguage, String country, Content content) throws ConsentManagerException;

    Information getInformation(String id) throws EntityNotFoundException;

    Information findInformationByName(String name) throws EntityNotFoundException;

    CollectionPage<Treatment> listTreatments(TreatmentFilter filter);

    String createTreatment(String key, String name, String description, String defaultLanguage, String country) throws ConsentManagerException;

    Treatment getTreatment(String id) throws EntityNotFoundException;

    Treatment findTreatmentByName(String name) throws EntityNotFoundException;
}
