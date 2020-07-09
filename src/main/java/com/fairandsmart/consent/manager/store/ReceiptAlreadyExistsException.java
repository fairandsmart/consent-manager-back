package com.fairandsmart.consent.manager.store;

public class ReceiptAlreadyExistsException extends Exception {

    public ReceiptAlreadyExistsException(String s) {
        super(s);
    }

    public ReceiptAlreadyExistsException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
