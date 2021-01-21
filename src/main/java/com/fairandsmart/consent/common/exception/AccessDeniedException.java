package com.fairandsmart.consent.common.exception;

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

public class AccessDeniedException extends ConsentManagerException {

    public static final String ACCESS_TOKEN_ISSUE = "Access token is invalid, expired, missing or lacks some role";
    public static final String NO_ADMIN_ROLE = "Account lacks 'roles.admin' role" ;
    public static final String NO_OPERATOR_ROLE = "Account lacks 'roles.operator' role";
    public static final String NO_API_ROLE = "Account lacks 'roles.api' role";

    public AccessDeniedException() {
        super();
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }
}
