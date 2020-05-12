package com.fairandsmart.consent.manager.store;

public class ReceiptNotFoundException extends Exception {

    public ReceiptNotFoundException(String s) {
        super(s);
    }

    public ReceiptNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
