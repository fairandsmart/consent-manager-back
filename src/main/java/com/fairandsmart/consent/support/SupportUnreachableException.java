package com.fairandsmart.consent.support;

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
import com.fairandsmart.consent.common.exception.GenericException;

public class SupportUnreachableException extends GenericException {

    public static final String KEY = "supportError";

    public SupportUnreachableException(String message) {
        super(message);
    }

    public SupportUnreachableException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public ApiError.Type getType() {
        return ApiError.Type.SUPPORT_SERVICE_ERROR;
    }

}
