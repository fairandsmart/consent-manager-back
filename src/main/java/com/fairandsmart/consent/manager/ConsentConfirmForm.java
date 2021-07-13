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

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

public class ConsentConfirmForm {

    @Schema(description = "The form attached token")
    private String token;
    @Schema(description = "The expected confirmation type")
    private String type;
    private String language;
    private Map<String, String> params = new HashMap<>();
    private ConsentReceipt receipt;

    public ConsentConfirmForm() {
    }

    public ConsentConfirmForm(ConsentContext ctx) {
        this();
        this.type = ctx.getConfirmation().toString();
        this.language = ctx.getLanguage();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public ConsentConfirmForm withParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public ConsentReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(ConsentReceipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "ConsentConfirmForm{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
