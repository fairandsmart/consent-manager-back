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

import com.fairandsmart.consent.manager.entity.Transaction;
import com.fairandsmart.consent.manager.exception.ConsentContextSerializationException;

import java.net.URI;

public class ConsentTransaction {

    private String id;
    private String subject;
    private String state;
    private ConsentContext context;
    private String token;
    private URI task;
    private URI receipt;
    private URI create;
    private URI cpp;

    public ConsentTransaction() {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ConsentContext getContext() {
        return context;
    }

    public void setContext(ConsentContext context) {
        this.context = context;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public URI getTask() {
        return task;
    }

    public void setTask(URI formURI) {
        this.task = task;
    }

    public URI getReceipt() {
        return receipt;
    }

    public void setReceipt(URI receipt) {
        this.receipt = receipt;
    }

    public URI getCreate() {
        return create;
    }

    public void setCreate(URI create) {
        this.create = create;
    }

    public URI getCpp() {
        return cpp;
    }

    public void setCpp(URI cpp) {
        this.cpp = cpp;
    }

    public static ConsentTransaction fromTransaction(Transaction tx) throws ConsentContextSerializationException {
        ConsentTransaction dto = new ConsentTransaction();
        dto.setId(tx.id);
        dto.setSubject(tx.subject);
        dto.setState(tx.state.toString().toLowerCase());
        dto.setContext(tx.getConsentContext());
        return dto;
    }

    @Override
    public String toString() {
        return "ConsentTransaction{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", state='" + state + '\'' +
                ", context=" + context +
                ", token='" + token + '\'' +
                ", task=" + task +
                ", receipt=" + receipt +
                ", create=" + create +
                ", cpp=" + cpp +
                '}';
    }
}
