package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
 * #L%
 */

import com.fairandsmart.consent.manager.entity.ModelData;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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
