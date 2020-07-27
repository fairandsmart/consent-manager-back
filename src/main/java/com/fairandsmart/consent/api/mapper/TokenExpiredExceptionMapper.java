package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.token.TokenExpiredException;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenExpiredExceptionMapper implements ExceptionMapper<TokenExpiredException> {

    @Override
    public Response toResponse(TokenExpiredException exception) {
        ApiError error = new ApiError(HttpStatus.SC_BAD_REQUEST, "token-expired", exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
