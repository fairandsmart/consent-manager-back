package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.manager.entity.ModelData;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
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
