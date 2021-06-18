package com.fairandsmart.consent.api.dto;

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

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Transaction;
import com.fairandsmart.consent.manager.exception.ConsentContextSerializationException;

import java.net.URI;

public class TransactionDto {

    private String id;
    private String subject;
    private Transaction.State state;
    private ConsentContext context;
    private URI formURI;

    public TransactionDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Transaction.State getState() {
        return state;
    }

    public void setState(Transaction.State state) {
        this.state = state;
    }

    public ConsentContext getContext() {
        return context;
    }

    public void setContext(ConsentContext context) {
        this.context = context;
    }

    public URI getFormURI() {
        return formURI;
    }

    public void setFormURI(URI formURI) {
        this.formURI = formURI;
    }

    public static TransactionDto fromTransaction(Transaction tx) throws ConsentContextSerializationException {
        TransactionDto dto = new TransactionDto();
        dto.setId(tx.id);
        dto.setSubject(tx.subject);
        dto.setState(tx.state);
        dto.setContext(tx.getConsentContext());
        return dto;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", state=" + state +
                ", context=" + context +
                ", formURI=" + formURI +
                '}';
    }
}
