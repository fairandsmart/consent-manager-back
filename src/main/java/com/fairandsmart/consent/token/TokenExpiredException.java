package com.fairandsmart.consent.token;

public class TokenExpiredException extends Exception {
    public TokenExpiredException() {
    }

    public TokenExpiredException(String s) {
        super(s);
    }

    public TokenExpiredException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
