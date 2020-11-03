package com.fairandsmart.consent.manager;

import java.net.URI;

public class ConsentFormResult {
    ConsentContext context;
    URI receiptURI;

    public void setContext(ConsentContext context) {
        this.context = context;
    }

    public ConsentContext getContext() {
        return context;
    }

    public void setReceiptURI(URI receiptURI) {
        this.receiptURI = receiptURI;
    }

    public URI getReceiptURI() {
        return receiptURI;
    }

    @Override
    public String toString() {
        return "ConsentFormResult{" +
                "context=" + context +
                ", receiptURI=" + receiptURI +
                '}';
    }
}
