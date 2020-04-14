package com.fairandsmart.consent.token;

public class TokenServiceException extends Exception {
    public TokenServiceException(String message) {
        super(message);
    }

    public TokenServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TokenServiceException() {
    }
}
