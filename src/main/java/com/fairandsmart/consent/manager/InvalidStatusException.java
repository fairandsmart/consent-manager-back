package com.fairandsmart.consent.manager;

public class InvalidStatusException extends Exception {
    public InvalidStatusException(String s) {
        super(s);
    }

    public InvalidStatusException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
