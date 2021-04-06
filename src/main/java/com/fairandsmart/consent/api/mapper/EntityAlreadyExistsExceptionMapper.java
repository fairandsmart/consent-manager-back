package com.fairandsmart.consent.api.mapper;

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

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityAlreadyExistsExceptionMapper implements ExceptionMapper<EntityAlreadyExistsException> {

    @Inject
    MainConfig config;

    @Override
    public Response toResponse(EntityAlreadyExistsException exception) {
        ApiError error = new ApiError(ApiError.Type.ENTITY_ALREADY_EXISTS).withInstance(config.instance()).withException(exception);
        return Response.status(error.getStatus()).header(ApiError.API_ERROR_HEADER, error.getType()).entity(error).build();
    }
}
