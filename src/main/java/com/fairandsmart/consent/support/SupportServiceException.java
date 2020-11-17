package com.fairandsmart.consent.support;

public class SupportServiceException extends Exception {

    public SupportServiceException(String message) {
        super(message);
    }

    public SupportServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
