package com.fairandsmart.consent.manager;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class ConsentConfirmForm {

    @Schema(description = "The form attached token", example = "")
    private String token;
    @Schema(description = "The expected confirmation type", example = "")
    private String type;
    private String language;

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

    @Override
    public String toString() {
        return "ConsentConfirmForm{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
