package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import java.util.Arrays;

public class ConsentNotification {

    private String locale;
    private String token;
    private String recipient;
    private String url;
    private ModelVersion model;
    private boolean preview = false;
    private ModelVersion theme;
    private String receiptName;
    private String receiptType;
    private byte[] receipt;

    public ConsentNotification() {
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

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public byte[] getReceipt() {
        return receipt;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "ConsentNotification{" +
                "locale='" + locale + '\'' +
                ", token='" + token + '\'' +
                ", recipient='" + recipient + '\'' +
                ", url='" + url + '\'' +
                ", model=" + model +
                ", preview=" + preview +
                ", theme=" + theme +
                ", receiptType='" + receiptType + '\'' +
                ", receiptName='" + receiptName + '\'' +
                '}';
    }
}