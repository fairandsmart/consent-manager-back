package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityAlreadyExistsExceptionMapper implements ExceptionMapper<EntityAlreadyExistsException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(EntityAlreadyExistsException exception) {
        ApiError error = new ApiError(ApiError.Type.ENTITY_ALREADY_EXISTS).withInstance(instance.get()).withException(exception);
        return Response.status(error.getStatus()).entity(error).build();
    }
}

