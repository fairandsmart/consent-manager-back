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
import com.fairandsmart.consent.common.exception.GenericException;

public class InvalidStatusException extends GenericException {

    public static final String KEY = "invalidStatus";

    public InvalidStatusException(String s) {
        super(s);
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public ApiError.Type getType() {
        return ApiError.Type.UNEXPECTED_ERROR;
    }

}
