package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.entity.Content;
import com.fairandsmart.consent.manager.entity.Information;
import com.fairandsmart.consent.manager.filter.InformationFilter;

public interface ConsentService {

    CollectionPage<Information> listInformations(InformationFilter filter);

    String createInformation(Information.Type type, String name, String description, String defaultLanguage, String country, Content content) throws ConsentManagerException;

    Information getInformation(String id) throws EntityNotFoundException;

}
