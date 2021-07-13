package com.fairandsmart.consent.manager;

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

import com.fairandsmart.consent.common.exception.ClientException;
import com.fairandsmart.consent.common.exception.GenericException;

public class ConsentFormError {

    private Throwable error;
    private String language;
    private String key;
    private ConsentContext context;
    private String retryURI;

    public ConsentFormError(Throwable e) {
        this.error = e;
        if (e instanceof GenericException) {
            this.key = ((GenericException) e).getKey();
        }
        if (e instanceof ClientException) {
            this.context = ((ClientException)e).getContext();
            this.retryURI = ((ClientException)e).getRetryURI();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public ConsentContext getContext() {
        return context;
    }

    public void setContext(ConsentContext context) {
        this.context = context;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRetryURI() {
        return retryURI;
    }

    public void setRetryURI(String retryURI) {
        this.retryURI = retryURI;
    }

    @Override
    public String toString() {
        return "ConsentFormError{" +
                "error=" + error +
                ", language='" + language + '\'' +
                ", key='" + key + '\'' +
                ", context=" + context +
                ", retryURI='" + retryURI + '\'' +
                '}';
    }
}
