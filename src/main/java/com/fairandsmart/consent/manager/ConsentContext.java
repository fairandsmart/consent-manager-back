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
import com.fairandsmart.consent.token.Tokenizable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.*;

@Schema(description = "Consent form context object")
public class ConsentContext implements Tokenizable {

    private static final String DEFAULT_VALIDITY = "P6M";
    private static final long DEFAULT_VALIDITY_MILLIS = 15778800000L;
    private static final FormType DEFAULT_FORM_TYPE = FormType.FULL;
    private static final CollectionMethod DEFAULT_COLLECTION_METHOD = CollectionMethod.WEBFORM;
    private static final String USERINFOS_PREFIX = "userinfos_";
    private static final String ATTRIBUTES_PREFIX = "attributes_";

    @NotNull
    @Schema(description = "The customer unique ID", example = Placeholders.SHELDON)
    private String subject;
    @NotNull
    private ConsentForm.Orientation orientation;
    @Schema(description = "Consent form heading ID to use", example = "basicinfo.001")
    private String info;
    @NotNull
    @NotEmpty
    @Schema(description = "Included processing IDs", example = "[\"processing.001\"]")
    private List<String> elements;
    @Schema(description = "Redirect URL after consent is given / receipt is displayed", example = "http://www.fairandsmart.com")
    private String callback;
    @Schema(description = "Display language", example = "fr")
    private String language;
    @Schema(description = "Consent lifetime", example = "P6M") // TODO : explain the format
    private String validity;
    private FormType formType;
    private ReceiptDisplayType receiptDisplayType;
    @Schema(example = "{}")
    private Map<String, String> userinfos; // TODO : doc this
    @Schema(example = "{}")
    private Map<String, String> attributes; // TODO : doc this
    @Schema(description = "Email model ID (mandatory when email notification is needed)", example = "email.001")
    private String notificationModel;
    @Schema(description = "Email recipient (mandatory when email notification is needed)", example = Placeholders.SHELDON_AT_LOCALHOST)
    private String notificationRecipient;
    private CollectionMethod collectionMethod;
    @Schema(example = "")
    private String author; // TODO : doc this
    @Schema(example = "false")
    private boolean preview = false; // TODO : doc this
    @Schema(description = "include iFrameResizer.js in consent page")
    private boolean iframe = false;
    @Schema(description = "Consent form theme ID to use", example = "")
    private String theme;
    @Schema(hidden = true)
    private String receiptId;
    @Schema(description = "Display an \"accept all\" button ?", example = "false")
    private boolean acceptAllVisible = false;
    @Schema(description = "Display the consent validity ?")
    private boolean validityVisible = true;
    @Schema(description = "\"accept all\" button label", example = "")
    private String acceptAllText;
    @Schema(description = "move footer to to of iframe", example = "false")
    private boolean footerOnTop = false; // TODO : doc this

    public ConsentContext() {
        this.elements = new ArrayList<>();
        this.userinfos = new HashMap<>();
        this.attributes = new HashMap<>();
        this.validity = DEFAULT_VALIDITY;
        this.formType  = DEFAULT_FORM_TYPE;
        this.collectionMethod = DEFAULT_COLLECTION_METHOD;
    }

    public String getSubject() {
        return subject;
    }

    public ConsentContext setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public ConsentForm.Orientation getOrientation() {
        return orientation;
    }

    public ConsentContext setOrientation(ConsentForm.Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public ConsentContext setInfo(String info) {
        this.info = info;
        return this;
    }

    public List<String> getElements() {
        return elements;
    }

    @Schema(hidden = true)
    public String getElementsString() {
        return String.join(",", elements);
    }

    @Schema(hidden = true)
    public void setElementsString(String elements) {
        this.setElements(Arrays.asList(elements.split(",")));
    }

    public ConsentContext setElements(List<String> elements) {
        this.elements = elements;
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

    public String getValidity() {
        return validity;
    }

    public ConsentContext setValidity(String validity) {
        if (validity.isEmpty()) {
            this.validity = DEFAULT_VALIDITY;
        } else {
            try {
                DatatypeFactory.newInstance().newDuration(validity);
                this.validity = validity;
            } catch (DatatypeConfigurationException e) {
                this.validity = DEFAULT_VALIDITY;
            }
        }
        return this;
    }

    @JsonIgnore
    public long getValidityInMillis() {
        try {
            return DatatypeFactory.newInstance().newDuration(validity).getTimeInMillis(new Date(0));
        } catch (DatatypeConfigurationException e) {
            return DEFAULT_VALIDITY_MILLIS;
        }
    }

    public FormType getFormType() {
        return formType;
    }

    public ConsentContext setFormType(FormType formType) {
        this.formType = formType;
        return this;
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

    public String getNotificationModel() {
        return notificationModel;
    }

    public ConsentContext setNotificationModel(String notificationModel) {
        this.notificationModel = notificationModel;
        return this;
    }

    public String getNotificationRecipient() {
        return notificationRecipient;
    }

    public ConsentContext setNotificationRecipient(String notificationRecipient) {
        this.notificationRecipient = notificationRecipient;
        return this;
    }

    public CollectionMethod getCollectionMethod() {
        return collectionMethod;
    }

    public ConsentContext setCollectionMethod(CollectionMethod collectionMethod) {
        this.collectionMethod = collectionMethod;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isPreview() {
        return preview;
    }

    public ConsentContext setPreview(boolean preview) {
        this.preview = preview;
        return this;
    }

    public boolean isIframe() {
        return iframe;
    }

    public ConsentContext setIframe(boolean iframe) {
        this.iframe = iframe;
        return this;
    }

    public String getTheme() {
        return theme;
    }

    public ConsentContext setTheme(String theme) {
        this.theme = theme;
        return this;
    }

    public ReceiptDisplayType getReceiptDisplayType() {
        return receiptDisplayType;
    }

    public ConsentContext setReceiptDisplayType(ReceiptDisplayType receiptDisplayType) {
        this.receiptDisplayType = receiptDisplayType;
        return this;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public ConsentContext setReceiptId(String receiptId) {
        this.receiptId = receiptId;
        return this;
    }

    public boolean isAcceptAllVisible() {
        return acceptAllVisible;
    }

    public ConsentContext setAcceptAllVisible(boolean acceptAllVisible) {
        this.acceptAllVisible = acceptAllVisible;
        return this;
    }

    public String getAcceptAllText() {
        return acceptAllText;
    }

    public ConsentContext setAcceptAllText(String acceptAllText) {
        this.acceptAllText = acceptAllText;
        return this;
    }

    public boolean isFooterOnTop() {
        return footerOnTop;
    }

    public ConsentContext setFooterOnTop(boolean footerOnTop) {
        this.footerOnTop = footerOnTop;
        return this;
    }

    public boolean isValidityVisible() {
        return validityVisible;
    }

    public ConsentContext setValidityVisible(boolean validityVisible) {
        this.validityVisible = validityVisible;
        return this;
    }

    @Override
    @Schema(hidden = true)
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (info != null) {
            claims.put("info", this.getInfo());
        }
        if (elements != null && !elements.isEmpty()) {
            claims.put("elements", this.getElementsString());
        }
        if (language != null) {
            claims.put("language", this.getLanguage());
        }
        if (validity != null) {
            claims.put("validity", this.getValidity());
        }
        if (callback != null) {
            claims.put("callback", this.getCallback());
        }
        if (orientation != null) {
            claims.put("orientation", this.getOrientation().name());
        }
        if (formType != null) {
            claims.put("formType", this.getFormType().name());
        }
        if (receiptDisplayType != null) {
            claims.put("receiptDisplayType", this.getReceiptDisplayType().toString());
        }
        if (notificationModel != null) {
            claims.put("notificationModel", this.getNotificationModel());
        }
        if (notificationRecipient != null) {
            claims.put("notificationRecipient", this.getNotificationRecipient());
        }
        if (collectionMethod != null) {
            claims.put("collectionMethod", this.getCollectionMethod().name());
        }
        if (theme != null) {
            claims.put("theme", this.getTheme());
        }
        if (receiptId != null) {
            claims.put("receiptId", this.getReceiptId());
        }
        if (acceptAllText != null) {
            claims.put("acceptAllText", this.getAcceptAllText());
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
        claims.put("acceptAllVisible", Boolean.toString(this.isAcceptAllVisible()));
        claims.put("preview", Boolean.toString(this.isPreview()));
        claims.put("iframe", Boolean.toString(this.isIframe()));
        claims.put("footerOnTop", Boolean.toString(this.isFooterOnTop()));
        claims.put("validityVisible", Boolean.toString(this.isValidityVisible()));
        return claims;
    }

    @Override
    @Schema(hidden = true)
    public Tokenizable setClaims(Map<String, String> claims) {
        if (claims.containsKey("info")) {
            this.setInfo(claims.get("info"));
        }
        if (claims.containsKey("elements")) {
            this.setElementsString(claims.get("elements"));
        }
        if (claims.containsKey("language")) {
            this.setLanguage(claims.get("language"));
        }
        if (claims.containsKey("validity")) {
            this.setValidity(claims.get("validity"));
        }
        if (claims.containsKey("callback")) {
            this.setCallback(claims.get("callback"));
        }
        if (claims.containsKey("orientation")) {
            this.setOrientation(ConsentForm.Orientation.valueOf(claims.get("orientation")));
        }
        if (claims.containsKey("formType")) {
            this.setFormType(FormType.valueOf(claims.get("formType")));
        }
        if (claims.containsKey("receiptDisplayType")) {
            this.setReceiptDisplayType(ReceiptDisplayType.valueOfName(claims.get("receiptDisplayType")));
        }
        if (claims.containsKey("notificationModel")) {
            this.setNotificationModel(claims.get("notificationModel"));
        }
        if (claims.containsKey("notificationRecipient")) {
            this.setNotificationRecipient(claims.get("notificationRecipient"));
        }
        if (claims.containsKey("collectionMethod")) {
            this.setCollectionMethod(CollectionMethod.valueOf(claims.get("collectionMethod")));
        }
        if (claims.containsKey("preview")) {
            this.setPreview(Boolean.parseBoolean(claims.get("preview")));
        }
        if (claims.containsKey("iframe")) {
            this.setIframe(Boolean.parseBoolean(claims.get("iframe")));
        }
        if (claims.containsKey("theme")) {
            this.setTheme(claims.get("theme"));
        }
        if (claims.containsKey("receiptId")) {
            this.setReceiptId(claims.get("receiptId"));
        }
        if (claims.containsKey("acceptAllVisible")) {
            this.setAcceptAllVisible(Boolean.parseBoolean(claims.get("acceptAllVisible")));
        }
        if (claims.containsKey("acceptAllText")) {
            this.setAcceptAllText(claims.get("acceptAllText"));
        }
        if (claims.containsKey("footerOnTop")) {
            this.setFooterOnTop(Boolean.parseBoolean(claims.get("footerOnTop")));
        }
        if (claims.containsKey("validityVisible")) {
            this.setValidityVisible(Boolean.parseBoolean(claims.get("validityVisible")));
        }
        claims.entrySet().stream().filter(entry -> entry.getKey().startsWith(USERINFOS_PREFIX)).forEach(
                entry -> this.getUserinfos().put(entry.getKey().substring(USERINFOS_PREFIX.length()), entry.getValue())
        );
        claims.entrySet().stream().filter(entry -> entry.getKey().startsWith(ATTRIBUTES_PREFIX)).forEach(
                entry -> this.getAttributes().put(entry.getKey().substring(ATTRIBUTES_PREFIX.length()), entry.getValue())
        );
        return this;
    }

    @Schema(description =
        "- PARTIAL form will only display elements that have no previous record;\n" +
        "- FULL form will display all elements;"
    )
    public enum FormType {
        PARTIAL,
        FULL
    }

    public enum ReceiptDisplayType {
        NONE("none"),
        HTML("text/html"),
        PDF("application/pdf"),
        XML("application/xml"),
        TEXT("text/plain");

        private final String name;

        ReceiptDisplayType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static ReceiptDisplayType valueOfName(String name) {
            for (ReceiptDisplayType e : values()) {
                if (e.name.equals(name)) {
                    return e;
                }
            }
            throw new IllegalArgumentException("Unknown format : " + name);
        }
    }

    @Schema(description =
        "- WEBFORM the user filled in a form;\n" +
        "- OPERATOR an operator created the record based on an interaction with the user;\n"
            , example = "WEBFORM"
    ) // TODO : others need to be documented
     public enum CollectionMethod {
        WEBFORM,
        OPERATOR,
        EMAIL,
        RECEIPT,
        USER_PAGE
    }

    @Override
    public String toString() {
        return "ConsentContext{" +
                "subject='" + subject + '\'' +
                ", orientation=" + orientation +
                ", info='" + info + '\'' +
                ", elements=" + elements +
                ", callback='" + callback + '\'' +
                ", language='" + language + '\'' +
                ", validity='" + validity + '\'' +
                ", formType=" + formType +
                ", receiptDisplayType=" + receiptDisplayType +
                ", userinfos=" + userinfos +
                ", attributes=" + attributes +
                ", notificationModel='" + notificationModel + '\'' +
                ", notificationRecipient='" + notificationRecipient + '\'' +
                ", collectionMethod=" + collectionMethod +
                ", author='" + author + '\'' +
                ", preview=" + preview +
                ", iframe=" + iframe +
                ", theme='" + theme + '\'' +
                ", receiptId='" + receiptId + '\'' +
                ", showAcceptAll=" + acceptAllVisible +
                ", showValidity=" + validityVisible +
                ", acceptAllText='" + acceptAllText + '\'' +
                ", footerOnTop=" + footerOnTop +
                '}';
    }

}
