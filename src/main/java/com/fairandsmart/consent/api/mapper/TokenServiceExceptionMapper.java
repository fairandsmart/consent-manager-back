package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.token.TokenServiceException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenServiceExceptionMapper implements ExceptionMapper<TokenServiceException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(TokenServiceException exception) {
        ApiError error = new ApiError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "unexpected-error", "Unexpected Token Exception").withInstance(instance.get())
                .withException(exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}
