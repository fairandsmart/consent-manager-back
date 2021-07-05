package com.fairandsmart.consent.manager.exception;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 *
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 *
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

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
