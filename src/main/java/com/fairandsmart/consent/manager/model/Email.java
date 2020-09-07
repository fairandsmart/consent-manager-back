package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.Objects;

public class Email extends ModelData {

    public static final String TYPE = "email";

    private String sender;
    private String subject;
    private String title;
    private String body;
    private String buttonLabel;
    private String footer;
    private String signature;

    public Email() {
        this.setType(TYPE);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Email withSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Email withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Email withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Email withBody(String body) {
        this.body = body;
        return this;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public Email withButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Email withFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Email withSignature(String signature) {
        this.signature = signature;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(sender, email.sender) &&
                Objects.equals(subject, email.subject) &&
                Objects.equals(title, email.title) &&
                Objects.equals(body, email.body) &&
                Objects.equals(buttonLabel, email.buttonLabel) &&
                Objects.equals(footer, email.footer) &&
                Objects.equals(signature, email.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, subject, title, body, buttonLabel, footer, signature);
    }

    @Override
    public String toString() {
        return "Email{" +
                "sender='" + sender + '\'' +
                ", subject='" + subject + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", buttonLabel='" + buttonLabel + '\'' +
                ", footer='" + footer + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
