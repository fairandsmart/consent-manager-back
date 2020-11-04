package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.manager.render.RenderingException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class RenderingExceptionMapper implements ExceptionMapper<RenderingException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(RenderingException exception) {
        ApiError error = new ApiError(ApiError.Type.RENDERER_ERROR).withInstance(instance.get()).withException(exception);
        return Response.status(error.getStatus()).entity(error).build();
    }
}
