package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.token.Tokenizable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.*;

public class ConsentContext implements Tokenizable {

    private static final String DEFAULT_VALIDITY = "P6M";
    private static final FormType DEFAULT_FORM_TYPE = FormType.FULL;
    private static final ReceiptDeliveryType DEFAULT_RECEIPT_DELIVERY = ReceiptDeliveryType.DISPLAY;
    private static final CollectionMethod DEFAULT_COLLECTION_METHOD = CollectionMethod.WEBFORM;
    private static final String USERINFOS_PREFIX = "userinfos_";
    private static final String ATTRIBUTES_PREFIX = "attributes_";

    @NotNull
    private String subject;
    @NotNull
    private ConsentForm.Orientation orientation;
    private String info;
    @NotNull
    @NotEmpty
    private List<String> elements;
    private String callback;
    private String locale;
    private String validity;
    private FormType formType;
    private ReceiptDeliveryType receiptDeliveryType;
    private Map<String, String> userinfos;
    private Map<String, String> attributes;
    private String notificationModel;
    private String notificationRecipient;
    private CollectionMethod collectionMethod;
    private String author;
    private boolean preview = false;
    private boolean iframe = false;
    private String theme;

    public ConsentContext() {
        this.elements = new ArrayList<>();
        this.userinfos = new HashMap<>();
        this.attributes = new HashMap<>();
        this.validity = DEFAULT_VALIDITY;
        this.formType  = DEFAULT_FORM_TYPE;
        this.receiptDeliveryType = DEFAULT_RECEIPT_DELIVERY;
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

    public String getElementsString() {
        return String.join(",", elements);
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

    public String getCallback() {
        return callback;
    }

    public ConsentContext setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getLocale() {
        if (StringUtils.isEmpty(locale)) {
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

    @Override
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (info != null) {
            claims.put("info", this.getInfo());
        }
        if (elements != null && !elements.isEmpty()) {
            claims.put("elements", this.getElementsString());
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
        if (notificationModel != null) {
            claims.put("notificationModel", this.getNotificationModel());
        }
        if (notificationRecipient != null) {
            claims.put("notificationRecipient", this.getNotificationRecipient());
        }
        if (collectionMethod != null) {
            claims.put("collectionMethod", this.getCollectionMethod().name());
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
        if (theme != null) {
            claims.put("theme", this.getTheme());
        }
        return claims;
    }

    @Override
    public Tokenizable setClaims(Map<String, String> claims) {
        if (claims.containsKey("info")) {
            this.setInfo(claims.get("info"));
        }
        if (claims.containsKey("elements")) {
            this.setElementsString(claims.get("elements"));
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

    /**
     * WEBFORM the user filled in a form
     * OPERATOR an operator created the record based on an interaction with the user
     */
    public enum CollectionMethod {
        WEBFORM,
        OPERATOR
    }

    @Override
    public String toString() {
        return "ConsentContext{" +
                "subject='" + subject + '\'' +
                ", orientation=" + orientation +
                ", info='" + info + '\'' +
                ", elements=" + elements +
                ", callback='" + callback + '\'' +
                ", validity='" + validity + '\'' +
                ", locale='" + locale + '\'' +
                ", formType=" + formType +
                ", receiptDeliveryType=" + receiptDeliveryType +
                ", userinfos=" + userinfos +
                ", attributes=" + attributes +
                ", notificationEmailModel='" + notificationModel + '\'' +
                ", notificationEmailRecipient='" + notificationRecipient + '\'' +
                ", collectionMethod=" + collectionMethod +
                ", author='" + author + '\'' +
                ", preview=" + preview +
                ", iframe=" + iframe +
                ", theme='" + theme + '\'' +
                '}';
    }
}
