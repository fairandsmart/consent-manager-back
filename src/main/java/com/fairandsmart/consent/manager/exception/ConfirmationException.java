package com.fairandsmart.consent.manager.exception;

import com.fairandsmart.consent.api.error.ApiError;
import com.fairandsmart.consent.common.exception.ClientException;
import com.fairandsmart.consent.manager.ConsentContext;

public class ConfirmationException extends ClientException {

    public static final String KEY = "confirmationError";

    private ConsentContext ctx;

    public ConfirmationException(ConsentContext ctx, String message) {
        super(message);
        this.ctx = ctx;
    }

    @Override
    public ConsentContext getContext() {
        return ctx;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public ApiError.Type getType() {
        return ApiError.Type.CONFIRMATION_ERROR;
    }

}
