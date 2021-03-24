package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.common.exception.ReportableException;
import com.fairandsmart.consent.common.exception.NamedException;
import com.fairandsmart.consent.common.exception.RetryableException;

public class ConsentFormError {

    private Throwable error;
    private String language;
    private String key;
    private ConsentContext context;
    private String retryURI;


    public ConsentFormError(Throwable e) {
        this.error = e;
        if (e instanceof NamedException) {
            this.key = ((NamedException)e).getKey();
        }
        if (e instanceof ReportableException) {
            this.context = ((ReportableException)e).getContext();
        }
        if (e instanceof RetryableException) {
            this.retryURI = ((RetryableException)e).getRetryURI();
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
