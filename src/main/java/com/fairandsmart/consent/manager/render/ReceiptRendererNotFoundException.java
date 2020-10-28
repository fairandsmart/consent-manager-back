package com.fairandsmart.consent.manager.render;

public class ReceiptRendererNotFoundException extends Exception {

    public ReceiptRendererNotFoundException(String message) {
        super(message);
    }

    public ReceiptRendererNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
