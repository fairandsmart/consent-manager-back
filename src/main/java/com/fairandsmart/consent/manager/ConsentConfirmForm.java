package com.fairandsmart.consent.manager;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

public class ConsentConfirmForm {

    @Schema(description = "The form attached token", example = "")
    private String token;
    @Schema(description = "The expected confirmation type", example = "")
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
