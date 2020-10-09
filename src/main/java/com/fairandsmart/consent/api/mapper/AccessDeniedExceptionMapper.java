package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(AccessDeniedException exception) {
        ApiError error = new ApiError(HttpStatus.SC_UNAUTHORIZED, "access-denied", "Access Denied").withInstance(instance.get())
                .withException(exception);
        return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
    }
}
