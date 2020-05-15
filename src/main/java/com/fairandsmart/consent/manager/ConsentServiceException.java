package com.fairandsmart.consent.manager;

public class ConsentServiceException extends Exception {
    public ConsentServiceException(String s) {
        super(s);
    }

    public ConsentServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
