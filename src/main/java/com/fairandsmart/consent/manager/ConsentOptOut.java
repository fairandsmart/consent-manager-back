package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsentOptOut {

    private String locale;
    private String token;
    private String recipient;
    private String url;
    private ModelVersion model;
    private boolean preview = false;
    private ModelVersion theme;

    public ConsentOptOut() {
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ModelVersion getModel() {
        return model;
    }

    public void setModel(ModelVersion model) {
        this.model = model;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public ModelVersion getTheme() {
        return theme;
    }

    public void setTheme(ModelVersion theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "ConsentEmail{" +
                "locale='" + locale + '\'' +
                ", token='" + token + '\'' +
                ", recipient='" + recipient + '\'' +
                ", url='" + url + '\'' +
                ", model=" + model +
                ", preview=" + preview +
                ", theme=" + theme +
                '}';
    }
}
