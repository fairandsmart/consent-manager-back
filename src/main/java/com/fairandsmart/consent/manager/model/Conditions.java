package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conditions that = (Conditions) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(body, that.body) &&
                Objects.equals(acceptLabel, that.acceptLabel) &&
                Objects.equals(rejectLabel, that.rejectLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, body, acceptLabel, rejectLabel);
    }

    @Override
    public String toString() {
        return "Conditions{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", acceptLabel='" + acceptLabel + '\'' +
                ", rejectLabel='" + rejectLabel + '\'' +
                '}';
    }
}
