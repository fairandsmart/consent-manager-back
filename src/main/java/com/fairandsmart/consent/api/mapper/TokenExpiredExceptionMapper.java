package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.token.TokenExpiredException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenExpiredExceptionMapper implements ExceptionMapper<TokenExpiredException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(TokenExpiredException exception) {
        ApiError error = new ApiError(ApiError.Type.TOKEN_EXPIRED).withInstance(instance.get()).withException(exception);
        return Response.status(error.getStatus()).entity(error).build();
    }
}
