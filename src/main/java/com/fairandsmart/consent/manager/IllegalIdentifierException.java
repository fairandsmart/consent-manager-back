package com.fairandsmart.consent.manager;

public class IllegalIdentifierException extends Exception {
    public IllegalIdentifierException(String s) {
        super(s);
    }

    public IllegalIdentifierException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
