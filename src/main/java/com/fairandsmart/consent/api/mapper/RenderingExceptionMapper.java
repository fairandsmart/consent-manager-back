package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.manager.render.RenderingException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class RenderingExceptionMapper implements ExceptionMapper<RenderingException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(RenderingException exception) {
        ApiError error = new ApiError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "rendering-error", "Rendering Error").withInstance(instance.get())
                .withException(exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}
