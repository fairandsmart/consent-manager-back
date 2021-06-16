package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import java.net.URI;

public class ConsentAcknowledgment {

    private ConsentContext context;
    private URI receiptURI;

    public ConsentAcknowledgment() {
    }

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
