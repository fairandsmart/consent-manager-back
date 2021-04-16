package com.fairandsmart.consent.manager;

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

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.manager.model.FormLayout;
import com.fairandsmart.consent.token.Tokenizable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.*;
import java.util.stream.Collectors;

@Schema(description = "Consent form context object")
public class ConsentContext implements Tokenizable {

    private static final String DEFAULT_VALIDITY = "P6M";
    private static final long DEFAULT_VALIDITY_MILLIS = 15778800000L;
    private static final String USERINFOS_PREFIX = "userinfos_";
    private static final String ATTRIBUTES_PREFIX = "attributes_";

    @NotNull
    @Schema(description = "The customer unique identifier", example = Placeholders.SHELDON)
    private String subject;
    @Schema(description = "Redirect URL after consent is given / receipt is displayed", example = "http://www.fairandsmart.com")
    private String callback;
    @Schema(description = "Display language", example = "fr")
    private String language;
    @Schema(description = "Consent collection origin", example = "webform")
    private Origin origin;
    @Schema(description = "Email recipient (mandatory when email notification is needed)", example = Placeholders.SHELDON_AT_LOCALHOST)
    private String notificationRecipient;
    @Schema(description = "Consent lifetime", example = "P6M") // TODO : explain the format
    private String validity;
    @Schema(example = "{}")
    private Map<String, String> userinfos; // TODO : doc this
    @Schema(example = "{}")
    private Map<String, String> attributes; // TODO : doc this
    @Schema(hidden = true)
    private String transaction;
    @Schema(description = "Consent form layout key to use", example = "layout.001")
    private String layout;
    @Schema(description = "Consent form layout data to use")
    private FormLayout layoutData;
    @Schema(description = "This context will be used to generate a form preview", example = "false")
    private boolean preview = false;
    @Schema(description = "The consent author (in case the consent is set by an operator)")
    private String author;

    public enum Origin {
        WEBFORM,
        RECEIPT,
        USER,
        EMAIL,
        OPERATOR
    }

    public ConsentContext() {
        this.userinfos = new HashMap<>();
        this.attributes = new HashMap<>();
        this.validity = DEFAULT_VALIDITY;
        this.origin = Origin.WEBFORM;
    }

    public String getSubject() {
        return subject;
    }

    public ConsentContext setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getCallback() {
        return callback;
    }

    public ConsentContext setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getLanguage() {
        if (StringUtils.isEmpty(language)) {
            return Locale.getDefault().toLanguageTag();
        }
        return language;
    }

    public ConsentContext setLanguage(String language) {
        this.language = language;
        return this;
    }

    public Origin getOrigin() {
        return origin;
    }

    public ConsentContext setOrigin(Origin origin) {
        this.origin = origin;
        return this;
    }

    public String getValidity() {
        return validity;
    }

    public ConsentContext setValidity(String validity) {
        if (validity.isEmpty()) {
            this.validity = DEFAULT_VALIDITY;
        } else {
            try {
                DatatypeFactory.newInstance().newDuration(getParseableValidity());
                this.validity = validity;
            } catch (DatatypeConfigurationException e) {
                this.validity = DEFAULT_VALIDITY;
            }
        }
        return this;
    }

    @JsonIgnore
    private String getParseableValidity() {
        if (validity.contains("W")) {
            return "P" + 7 * Integer.parseInt(validity.substring(1, validity.length() - 1))+ "D";
        }
        return validity;
    }

    @JsonIgnore
    public long getValidityInMillis() {
        try {
            return DatatypeFactory.newInstance().newDuration(getParseableValidity()).getTimeInMillis(new Date(0));
        } catch (DatatypeConfigurationException e) {
            return DEFAULT_VALIDITY_MILLIS;
        }
    }

    public Map<String, String> getUserinfos() {
        return userinfos;
    }

    public ConsentContext setUserinfos(Map<String, String> userinfos) {
        this.userinfos = userinfos;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public ConsentContext setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public String getNotificationRecipient() {
        return notificationRecipient;
    }

    public ConsentContext setNotificationRecipient(String notificationRecipient) {
        this.notificationRecipient = notificationRecipient;
        return this;
    }

    public String getTransaction() {
        return transaction;
    }

    public ConsentContext setTransaction(String transaction) {
        this.transaction = transaction;
        return this;
    }

    public String getLayout() {
        return layout;
    }

    public ConsentContext setLayout(String layout) {
        this.layout = layout;
        return this;
    }

    public FormLayout getLayoutData() {
        return layoutData;
    }

    public ConsentContext setLayoutData(FormLayout layoutData) {
        this.layoutData = layoutData;
        return this;
    }

    public boolean isPreview() {
        return preview;
    }

    public ConsentContext setPreview(boolean preview) {
        this.preview = preview;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public ConsentContext setAuthor(String author) {
        this.author = author;
        return this;
    }

    @Override
    @Schema(hidden = true)
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (language != null) {
            claims.put("language", this.getLanguage());
        }
        if (validity != null) {
            claims.put("validity", this.getValidity());
        }
        if (callback != null) {
            claims.put("callback", this.getCallback());
        }
        if (notificationRecipient != null) {
            claims.put("notificationRecipient", this.getNotificationRecipient());
        }
        if (origin != null) {
            claims.put("origin", this.getOrigin().toString());
        }
        if (userinfos != null && !userinfos.isEmpty()) {
            for (Map.Entry<String, String> entry : userinfos.entrySet()) {
                claims.put(USERINFOS_PREFIX + entry.getKey(), entry.getValue());
            }
        }
        if (attributes != null && !attributes.isEmpty()) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                claims.put(ATTRIBUTES_PREFIX + entry.getKey(), entry.getValue());
            }
        }
        if (transaction != null) {
            claims.put("transaction", this.getTransaction());
        }
        if (layout != null) {
            claims.put("layout", this.getLayout());
        }
        if (layoutData != null) {
            Map<String, String> layout = this.getLayoutData().getClaims();
            claims.putAll(layout.entrySet().stream().collect(Collectors.toMap(entry -> "layout.".concat(entry.getKey()),Map.Entry::getValue)));
        }
        claims.put("preview", Boolean.toString(this.isPreview()));
        if (author != null) {
            claims.put("author", this.getAuthor());
        }
        return claims;
    }

    @Override
    @Schema(hidden = true)
    public Tokenizable setClaims(Map<String, String> claims) {
        if (claims.containsKey("language")) {
            this.setLanguage(claims.get("language"));
        }
        if (claims.containsKey("validity")) {
            this.setValidity(claims.get("validity"));
        }
        if (claims.containsKey("callback")) {
            this.setCallback(claims.get("callback"));
        }
        if (claims.containsKey("notificationRecipient")) {
            this.setNotificationRecipient(claims.get("notificationRecipient"));
        }
        if (claims.containsKey("origin")) {
            this.setOrigin(Origin.valueOf(claims.get("origin")));
        }
        claims.entrySet().stream().filter(entry -> entry.getKey().startsWith(USERINFOS_PREFIX)).forEach(
                entry -> this.getUserinfos().put(entry.getKey().substring(USERINFOS_PREFIX.length()), entry.getValue())
        );
        claims.entrySet().stream().filter(entry -> entry.getKey().startsWith(ATTRIBUTES_PREFIX)).forEach(
                entry -> this.getAttributes().put(entry.getKey().substring(ATTRIBUTES_PREFIX.length()), entry.getValue())
        );
        if (claims.containsKey("transaction")) {
            this.setTransaction(claims.get("transaction"));
        }
        if (claims.containsKey("layout")) {
            this.setLayout(claims.get("layout"));
        }
        Map<String, String> overlayingClaims = claims.entrySet().stream().filter(e -> e.getKey().startsWith("layout.")).collect(Collectors.toMap(e -> e.getKey().substring("layout.".length()), Map.Entry::getValue));
        if (!overlayingClaims.isEmpty()) {
            FormLayout overlaying = new FormLayout();
            overlaying.setClaims(overlayingClaims);
            this.setLayoutData(overlaying);
        }
        if (claims.containsKey("preview")) {
            this.setPreview(Boolean.parseBoolean(claims.get("preview")));
        }
        if (claims.containsKey("author")) {
            this.setAuthor(claims.get("author"));
        }
        return this;
    }

    @Override
    public String toString() {
        return "ConsentContext{" +
                "subject='" + subject + '\'' +
                ", callback='" + callback + '\'' +
                ", language='" + language + '\'' +
                ", origin=" + origin +
                ", notificationRecipient='" + notificationRecipient + '\'' +
                ", validity='" + validity + '\'' +
                ", userinfos=" + userinfos +
                ", attributes=" + attributes +
                ", transaction='" + transaction + '\'' +
                ", layout='" + layout + '\'' +
                ", layoutData=" + layoutData +
                ", preview=" + preview +
                ", author='" + author + '\'' +
                '}';
    }
}
