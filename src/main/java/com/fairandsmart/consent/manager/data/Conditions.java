package com.fairandsmart.consent.manager.data;

public class Conditions extends ModelData {

    public static final String TYPE = "conditions";

    private String title;
    private String body;
    private String acceptLabel;
    private String rejectLabel;

    public Conditions() {
        this.setType(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Conditions withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Conditions withBody(String body) {
        this.body = body;
        return this;
    }

    public String getAcceptLabel() {
        return acceptLabel;
    }

    public void setAcceptLabel(String acceptLabel) {
        this.acceptLabel = acceptLabel;
    }

    public Conditions withAcceptLabel(String acceptLabel) {
        this.acceptLabel = acceptLabel;
        return this;
    }

    public String getRejectLabel() {
        return rejectLabel;
    }

    public void setRejectLabel(String rejectLabel) {
        this.rejectLabel = rejectLabel;
    }

    public Conditions withRejectLabel(String rejectLabel) {
        this.rejectLabel = rejectLabel;
        return this;
    }
}
