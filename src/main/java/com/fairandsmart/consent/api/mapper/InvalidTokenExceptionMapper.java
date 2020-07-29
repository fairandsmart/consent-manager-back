package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.token.InvalidTokenException;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException> {

    @Override
    public Response toResponse(InvalidTokenException exception) {
        ApiError error = new ApiError(HttpStatus.SC_BAD_REQUEST, "token-invalid", "Invalid Token")
                .withException(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
