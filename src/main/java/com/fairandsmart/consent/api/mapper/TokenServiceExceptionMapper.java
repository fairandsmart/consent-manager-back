package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenServiceException;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenServiceExceptionMapper implements ExceptionMapper<TokenServiceException> {

    @Override
    public Response toResponse(TokenServiceException exception) {
        ApiError error = new ApiError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "unexpected-error", exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}
