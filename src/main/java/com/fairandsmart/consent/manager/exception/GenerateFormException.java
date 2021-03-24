package com.fairandsmart.consent.manager.exception;

import com.fairandsmart.consent.common.exception.ReportableException;
import com.fairandsmart.consent.common.exception.NamedException;
import com.fairandsmart.consent.manager.ConsentContext;

public class GenerateFormException extends Exception implements NamedException, ReportableException {

    public static final String KEY = "generateForm";

    private ConsentContext ctx;

    public GenerateFormException(ConsentContext ctx, String message) {
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
}
