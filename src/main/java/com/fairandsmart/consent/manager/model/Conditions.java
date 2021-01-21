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
        this.body = Jsoup.clean(body, Whitelist.relaxed());
    }

    public Conditions withBody(String body) {
        this.body = Jsoup.clean(body, Whitelist.relaxed());
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
