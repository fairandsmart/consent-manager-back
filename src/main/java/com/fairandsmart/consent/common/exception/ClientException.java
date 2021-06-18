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

import com.fairandsmart.consent.manager.ConsentContext;

public abstract class ClientException extends GenericException {

    private ConsentContext ctx;
    private String retryURI;

    public ClientException() {
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public ConsentContext getContext() {
        return ctx;
    }

    public void setContext(ConsentContext ctx) {
        this.ctx = ctx;
    }

    public String getRetryURI() {
        return retryURI;
    }

    public void setRetryURI(String retryURI) {
        this.retryURI = retryURI;
    }

}
