package com.fairandsmart.consent.manager;

public class InvalidConsentException extends Exception {
    public InvalidConsentException(String s) {
        super(s);
    }

    public InvalidConsentException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
