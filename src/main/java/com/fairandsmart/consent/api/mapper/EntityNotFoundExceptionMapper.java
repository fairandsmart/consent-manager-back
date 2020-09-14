package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @ConfigProperty(name = "consent.instance.name")
    String instance;

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        ApiError error = new ApiError(HttpStatus.SC_NOT_FOUND, "not-found", "Entity Not Found").withInstance(instance)
                .withException(exception);
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}

