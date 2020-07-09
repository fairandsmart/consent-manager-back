package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException e) {
        ApiError error = new ApiError(HttpStatus.SC_UNAUTHORIZED, "access-denied", "Access Denied");
        return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
    }
}
