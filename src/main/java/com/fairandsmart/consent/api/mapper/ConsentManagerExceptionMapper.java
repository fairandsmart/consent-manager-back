package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConsentManagerExceptionMapper implements ExceptionMapper<ConsentManagerException> {

    @Override
    public Response toResponse(ConsentManagerException exception) {
        ApiError error = new ApiError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "unexpected-error", "Unexpected Error");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}
