package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityAlreadyExistsExceptionMapper implements ExceptionMapper<EntityAlreadyExistsException> {

    @ConfigProperty(name = "consent.instance.name")
    String instance;

    @Override
    public Response toResponse(EntityAlreadyExistsException exception) {
        ApiError error = new ApiError(HttpStatus.SC_CONFLICT, "already-exists", "Entity Already Exists").withInstance(instance)
                .withException(exception);
        return Response.status(Response.Status.CONFLICT).entity(error).build();
    }
}

