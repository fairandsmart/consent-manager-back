package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ReceiptNotFoundExceptionMapper implements ExceptionMapper<ReceiptNotFoundException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(ReceiptNotFoundException exception) {
        ApiError error = new ApiError(HttpStatus.SC_NOT_FOUND, "receipt-not-found", "Receipt Not Found").withInstance(instance.get())
                .withException(exception);
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
