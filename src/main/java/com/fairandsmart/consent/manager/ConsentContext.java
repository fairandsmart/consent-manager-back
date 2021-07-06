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
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Schema(description = "Consent form context object")
public class ConsentContext {

    private static final String DEFAULT_VALIDITY = "P6M";
    private static final long DEFAULT_VALIDITY_MILLIS = 15778800000L;

    @NotNull
    @Schema(description = "The customer unique identifier", example = Placeholders.SHELDON)
    private String subject;
    @Schema(description = "Redirect URL after consent is given", example = "http://www.fairandsmart.com")
    private String callback;
    @Schema(description = "Parent window domain, necessary for callback", example = "http://www.fairandsmart.com")
    private String iframeOrigin;
    @Schema(description = "Display language", example = "fr")
    private String language;
    @Schema(description = "Consent collection origin", example = "webform")
    private Origin origin;
    @Schema(description = "Email recipient (mandatory when email notification is needed)", example = Placeholders.SHELDON_AT_LOCALHOST)
    private String notificationRecipient;
    @Schema(description = "Consent lifetime (format: PnYnMnDTnHnMnS - XML Schema 1.0 section 3.2.6.1)", example = "P6M")
    private String validity;
    @Schema(example = "{}")
    private Map<String, String> userinfos; // TODO : doc this
    @Schema(example = "{}")
    private Map<String, String> attributes; // TODO : doc this
    @Schema(description = "Consent form layout key to use", example = "layout.001")
    private String layout;
    @Schema(description = "Consent form layout data to use")
    private FormLayout layoutData;
    @Schema(description = "This context will be used to generate a form preview", example = "false")
    private boolean preview = false;
    @Schema(description = "The consent author (in case the consent is set by an operator)")
    private String author;
    @Schema(description = "This context will need a user confirmation to commit transaction", example = Placeholders.CONFIRM_NONE)
    private Confirmation confirmation = Confirmation.NONE;
    @Schema(description = "The confirmation context elements")
    private Map<String, String> confirmationConfig;

    public enum Origin {
        WEBFORM,
        RECEIPT,
        USER,
        EMAIL,
        OPERATOR
    }

    public enum Confirmation {
        NONE (""),
        FORM_CODE ("code"),
        EMAIL_CODE ("code"),
        SMS_CODE ("code"),
        SIGNATURE ("sign"),
        AUDIO_RECORD ("audio"),
        VIDEO_RECORD ("video"),
        DIGITAL_SIGNATURE("digital-sign");

        private String type;

        Confirmation(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
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

    public String getIframeOrigin() {
        return iframeOrigin;
    }

    public ConsentContext setIframeOrigin(String iframeOrigin) {
        this.iframeOrigin = iframeOrigin;
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

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public ConsentContext setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
        return this;
    }

    public Map<String, String> getConfirmationConfig() {
        return confirmationConfig;
    }

    public ConsentContext setConfirmationConfig(Map<String, String> confirmationConfig) {
        this.confirmationConfig = confirmationConfig;
        return this;
    }

    @Override
    public String toString() {
        return "ConsentContext{" +
                "subject='" + subject + '\'' +
                ", callback='" + callback + '\'' +
                ", iframeOrigin='" + iframeOrigin + '\'' +
                ", language='" + language + '\'' +
                ", origin=" + origin +
                ", notificationRecipient='" + notificationRecipient + '\'' +
                ", validity='" + validity + '\'' +
                ", userinfos=" + userinfos +
                ", attributes=" + attributes +
                ", layout='" + layout + '\'' +
                ", layoutData=" + layoutData +
                ", preview=" + preview +
                ", author='" + author + '\'' +
                ", confirmation=" + confirmation +
                ", confirmationConfig=" + confirmationConfig +
                '}';
    }
}
