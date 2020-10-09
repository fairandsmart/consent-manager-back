package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.token.InvalidTokenException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(InvalidTokenException exception) {
        ApiError error = new ApiError(HttpStatus.SC_BAD_REQUEST, "token-invalid", "Invalid Token").withInstance(instance.get())
                .withException(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
