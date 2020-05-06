package com.fairandsmart.consent.token;

public class InvalidTokenException extends Exception {

    public InvalidTokenException(Throwable throwable) {
        super(throwable);
    }

    public InvalidTokenException(String s) {
        super(s);
    }
}
