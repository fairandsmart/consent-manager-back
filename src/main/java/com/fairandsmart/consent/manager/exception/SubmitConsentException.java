package com.fairandsmart.consent.manager.exception;

import com.fairandsmart.consent.common.exception.NamedException;
import com.fairandsmart.consent.common.exception.ReportableException;
import com.fairandsmart.consent.common.exception.RetryableException;
import com.fairandsmart.consent.manager.ConsentContext;

public class SubmitConsentException extends Exception implements NamedException, ReportableException, RetryableException {

    //TODO Use a super exception that includes all fields that could produce a specific html template and avoid those useless interfaces :
    //   JAX-RS allows to write a Mapper for super class
    //   We should create a global mapper and an exception handler that would be able to produce the

    public static final String KEY = "submitForm";

    private ConsentContext ctx;
    private String retryUri;

    public SubmitConsentException(ConsentContext ctx, String retryURI, String message) {
        super(message);
        this.ctx = ctx;
        this.retryUri = retryURI;
    }

    public SubmitConsentException(ConsentContext ctx, String retryURI, Throwable throwable) {
        super(throwable);
        this.ctx = ctx;
        this.retryUri = retryURI;
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
    public String getRetryURI() {
        return retryUri;
    }
}
