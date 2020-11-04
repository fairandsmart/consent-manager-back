package com.fairandsmart.consent.api.mapper;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ReceiptNotFoundExceptionMapper implements ExceptionMapper<ReceiptNotFoundException> {

    @ConfigProperty(name = "consent.instance.name")
    Instance<String> instance;

    @Override
    public Response toResponse(ReceiptNotFoundException exception) {
        ApiError error = new ApiError(ApiError.Type.RECEIPT_NOT_FOUND).withInstance(instance.get()).withException(exception);
        return Response.status(error.getStatus()).entity(error).build();
    }
}
