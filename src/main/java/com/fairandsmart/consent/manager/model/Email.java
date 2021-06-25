package com.fairandsmart.consent.manager.model;

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

import com.fairandsmart.consent.manager.entity.ModelData;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

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
        this.title = Jsoup.clean(title, Whitelist.relaxed());
    }

    public Email withTitle(String title) {
        this.title = Jsoup.clean(title, Whitelist.relaxed());
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = Jsoup.clean(body, Whitelist.relaxed());
    }

    public Email withBody(String body) {
        this.body = Jsoup.clean(body, Whitelist.relaxed());
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
        this.footer = Jsoup.clean(footer, Whitelist.relaxed());
    }

    public Email withFooter(String footer) {
        this.footer = Jsoup.clean(footer, Whitelist.relaxed());
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = Jsoup.clean(signature, Whitelist.relaxed());
    }

    public Email withSignature(String signature) {
        this.signature = Jsoup.clean(signature, Whitelist.relaxed());
        return this;
    }

    @Override
    public List<Pattern> getAllowedValuesPatterns() {
        return Collections.emptyList();
    }

    @Override
    public String extractDataMimeType() {
        return MediaType.APPLICATION_JSON;
    }

    @Override
    public String toMimeContent() throws IOException {
        return this.toJson();
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
