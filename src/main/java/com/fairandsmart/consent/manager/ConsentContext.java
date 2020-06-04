package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.token.Tokenizable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.*;
import java.util.stream.Collectors;

public class ConsentContext implements Tokenizable {

    private static final String DEFAULT_VALIDITY = "P6M";
    private static final FormType DEFAULT_FORM_TYPE = FormType.FULL;
    private static final ReceiptDeliveryType DEFAULT_RECEIPT_DELIVERY = ReceiptDeliveryType.DISPLAY;
    private static final String USERINFOS_PREFIX = "userinfos_";
    private static final String ATTRIBUTES_PREFIX = "attributes_";

    @NotNull
    private String subject;
    private String owner;
    @NotNull
    private ConsentForm.Orientation orientation;
    private String header;
    @NotNull
    @NotEmpty
    private List<String> elements;
    private String footer;
    private String callback;
    private String locale;
    private String validity;
    private FormType formType;
    private ReceiptDeliveryType receiptDeliveryType;
    private Map<String, String> userinfos;
    private Map<String, String> attributes;
    private String optoutEmail;
    private boolean preview = false;
    private boolean iframe = false;

    public ConsentContext() {
        this.elements = new ArrayList<>();
        this.userinfos = new HashMap<>();
        this.attributes = new HashMap<>();
        this.validity = DEFAULT_VALIDITY;
        this.formType  = DEFAULT_FORM_TYPE;
        this.receiptDeliveryType = DEFAULT_RECEIPT_DELIVERY;
    }

    public String getSubject() {
        return subject;
    }

    public ConsentContext setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public ConsentContext setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public ConsentForm.Orientation getOrientation() {
        return orientation;
    }

    public ConsentContext setOrientation(ConsentForm.Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public ConsentContext setHeader(String header) {
        this.header = header;
        return this;
    }

    public List<String> getElements() {
        return elements;
    }

    public String getElementsString() {
        return elements.stream().collect(Collectors.joining(","));
    }

    public void setElementsString(String elements) {
        this.setElements(Arrays.asList(elements.split(",")));
    }

    public ConsentContext setElements(List<String> elements) {
        this.elements = elements;
        return this;
    }

    public ConsentContext addElement(String element) {
        this.elements.add(element);
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public ConsentContext setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public String getCallback() {
        return callback;
    }

    public ConsentContext setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getLocale() {
        if (locale == null || locale.isEmpty()) {
            return Locale.getDefault().toLanguageTag();
        }
        return locale;
    }

    public ConsentContext setLocale(String locale) {
        this.locale = locale;
        return this;
    }

    public String getValidity() {
        return validity;
    }

    public ConsentContext setValidity(String validity) {
        try {
            DatatypeFactory.newInstance().newDuration(validity);
            this.validity = validity;
        } catch (DatatypeConfigurationException e) {
            this.validity = DEFAULT_VALIDITY;
        }
        return this;
    }

    @JsonIgnore
    public long getValidityInMillis() throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newDuration(validity).getTimeInMillis(new Date(0));
    }

    public FormType getFormType() {
        return formType;
    }

    public ConsentContext setFormType(FormType formType) {
        this.formType = formType;
        return this;
    }

    public ReceiptDeliveryType getReceiptDeliveryType() {
        return receiptDeliveryType;
    }

    public ConsentContext setReceiptDeliveryType(ReceiptDeliveryType receiptDeliveryType) {
        this.receiptDeliveryType = receiptDeliveryType;
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

    public String getOptoutEmail() {
        return optoutEmail;
    }

    public ConsentContext setOptoutEmail(String optoutEmail) {
        this.optoutEmail = optoutEmail;
        return this;
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

    @Override
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (header != null) {
            claims.put("header", this.getHeader());
        }
        if (elements != null && !elements.isEmpty()) {
            claims.put("elements", this.getElementsString());
        }
        if (footer != null) {
            claims.put("footer", this.getFooter());
        }
        if (locale != null) {
            claims.put("locale", this.getLocale());
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
        if (receiptDeliveryType != null) {
            claims.put("receiptDeliveryType", this.getReceiptDeliveryType().name());
        }
        if (optoutEmail != null) {
            claims.put("optoutEmail", this.getOptoutEmail());
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
        claims.put("preview", Boolean.toString(this.isPreview()));
        claims.put("iframe", Boolean.toString(this.isIframe()));
        return claims;
    }

    @Override
    public Tokenizable setClaims(Map<String, String> claims) {
        if (claims.containsKey("header")) {
            this.setHeader(claims.get("header"));
        }
        if (claims.containsKey("elements")) {
            this.setElementsString(claims.get("elements"));
        }
        if (claims.containsKey("footer")) {
            this.setFooter(claims.get("footer"));
        }
        if (claims.containsKey("locale")) {
            this.setLocale(claims.get("locale"));
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
        if (claims.containsKey("receiptDeliveryType")) {
            this.setReceiptDeliveryType(ReceiptDeliveryType.valueOf(claims.get("receiptDeliveryType")));
        }
        if (claims.containsKey("optoutEmail")) {
            this.setOptoutEmail(claims.get("optoutEmail"));
        }
        if (claims.containsKey("preview")) {
            this.setPreview(Boolean.parseBoolean(claims.get("preview")));
        }
        if (claims.containsKey("iframe")) {
            this.setIframe(Boolean.parseBoolean(claims.get("iframe")));
        }
        claims.entrySet().stream().filter(entry -> entry.getKey().startsWith(USERINFOS_PREFIX)).forEach(
                entry -> this.getUserinfos().put(entry.getKey().substring(USERINFOS_PREFIX.length()), entry.getValue())
        );
        claims.entrySet().stream().filter(entry -> entry.getKey().startsWith(ATTRIBUTES_PREFIX)).forEach(
                entry -> this.getAttributes().put(entry.getKey().substring(ATTRIBUTES_PREFIX.length()), entry.getValue())
        );
        return this;
    }

    /**
     * PARTIAL form will only display elements that have no previous record
     * FULL form will display all elements
     */
    public enum FormType {
        PARTIAL,
        FULL
    }

    /**
     * NONE no receipt is generated
     * GENERATE receipt is generated and stored server side, nothing is returned to caller
     * DISPLAY receipt html template is rendered to user with options to store or download
     * STORE receipt is stored in a cookie or local storage of the caller browser
     * DOWNLOAD receipt is forced to download by the caller
     */
    public enum ReceiptDeliveryType {
        NONE,
        GENERATE,
        DISPLAY,
        STORE,
        DOWNLOAD
    }

    @Override
    public String toString() {
        return "ConsentContext{" +
                "subject='" + subject + '\'' +
                ", owner='" + owner + '\'' +
                ", orientation=" + orientation +
                ", header='" + header + '\'' +
                ", elements=" + elements +
                ", footer='" + footer + '\'' +
                ", callback='" + callback + '\'' +
                ", validity='" + validity + '\'' +
                ", locale='" + locale + '\'' +
                ", formType=" + formType +
                ", receiptDeliveryType=" + receiptDeliveryType +
                ", userinfos=" + userinfos +
                ", attributes=" + attributes +
                ", optoutEmail='" + optoutEmail + '\'' +
                ", preview=" + preview +
                ", iframe=" + iframe +
                '}';
    }
}
