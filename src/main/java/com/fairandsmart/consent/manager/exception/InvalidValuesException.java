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

public class InvalidValuesException extends GenericException {

    public static final String KEY = "invalidValues";

    private String expected;
    private String found;

    public InvalidValuesException(String message, String expected, String found) {
        super(message.concat(" (expected: ").concat(expected).concat(", found: ").concat(found).concat(")"));
        this.expected = expected;
        this.found = found;
    }

    public InvalidValuesException(String message) {
        super(message);
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String excepted) {
        this.expected = excepted;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
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
