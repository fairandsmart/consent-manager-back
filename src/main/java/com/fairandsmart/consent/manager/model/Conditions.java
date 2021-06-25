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
import java.util.*;
import java.util.regex.Pattern;

public class Conditions extends ModelData {

    public static final String TYPE = "conditions";
    public static final List<Pattern> ALLOWED_VALUES_PATTERNS = new ArrayList<>();
    static {
        ALLOWED_VALUES_PATTERNS.add(Pattern.compile("accepted"));
        ALLOWED_VALUES_PATTERNS.add(Pattern.compile("refused"));
    }

    private String title;
    private String body;

    @Deprecated
    private String acceptLabel;

    @Deprecated
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
    public List<Pattern> getAllowedValuesPatterns() {
        return ALLOWED_VALUES_PATTERNS;
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
