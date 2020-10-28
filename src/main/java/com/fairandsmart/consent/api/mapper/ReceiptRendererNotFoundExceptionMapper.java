package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ReceiptRendererNotFoundExceptionMapper implements ExceptionMapper<ReceiptRendererNotFoundException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(ReceiptRendererNotFoundException exception) {
        ApiError error = new ApiError(HttpStatus.SC_BAD_REQUEST, "renderer-not-found", "Renderer Not Found").withInstance(instance.get())
                .withException(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
