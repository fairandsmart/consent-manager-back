package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ReceiptRendererNotFoundExceptionMapper implements ExceptionMapper<ReceiptRendererNotFoundException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(ReceiptRendererNotFoundException exception) {
        ApiError error = new ApiError(ApiError.Type.RENDERER_NOT_FOUND).withInstance(instance.get()).withException(exception);
        return Response.status(error.getStatus()).entity(error).build();
    }
}
