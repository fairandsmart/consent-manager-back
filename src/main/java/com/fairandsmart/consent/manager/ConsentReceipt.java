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

import com.fairandsmart.consent.common.util.ZonedDateTimeAdapter;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.model.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "receipt")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsentReceipt {

    private String transaction;
    private String jurisdiction;
    private String language;
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime date;
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime expirationDate;
    private String processor;
    private String subject;
    @XmlElementWrapper(name="subjectInfos")
    @XmlElement(name="subjectInfo")
    private List<NameValuePair> subjectDetails;
    private Controller dataController;
    private String headerNotice;
    @XmlElementWrapper(name="consents")
    @XmlElements({
            @XmlElement(name="consent", type=ProcessingConsent.class),
            @XmlElement(name="processingConsent", type=ProcessingConsent.class),
            @XmlElement(name="preferenceConsent", type=PreferenceConsent.class),
            @XmlElement(name="conditionsConsent", type=ConditionsConsent.class)
    })
    private List<Consent> consents;
    private String footerNotice;
    @XmlElementWrapper(name="attributes")
    @XmlElement(name="attribute")
    private List<NameValuePair> attributes;
    @XmlElementWrapper(name="attachments")
    @XmlElement(name="attachment")
    private List<Attachment> attachments;
    private String privacyPolicyUrl;
    private String collectionMethod;
    private String confirmation;
    private String updateUrl;
    private String updateUrlQrCode;
    private String notificationType;
    private String notificationRecipient;

    public ConsentReceipt() {
        subjectDetails = new ArrayList<>();
        consents = new ArrayList<>();
        attributes = new ArrayList<>();
        attachments = new ArrayList<>();
    }

    public ConsentReceipt(ConsentReceipt receipt) {
        this.transaction = receipt.transaction;
        this.jurisdiction = receipt.jurisdiction;
        this.language = receipt.language;
        this.date = receipt.date;
        this.expirationDate = receipt.expirationDate;
        this.processor = receipt.processor;
        this.subject = receipt.subject;
        this.subjectDetails = receipt.subjectDetails;
        this.dataController = receipt.dataController;
        this.headerNotice = receipt.headerNotice;
        this.consents = receipt.consents;
        this.footerNotice = receipt.footerNotice;
        this.confirmation = receipt.confirmation;
        this.attributes = receipt.attributes;
        this.attachments = receipt.attachments;
        this.privacyPolicyUrl = receipt.privacyPolicyUrl;
        this.collectionMethod = receipt.collectionMethod;
        this.updateUrl = receipt.updateUrl;
        this.updateUrlQrCode = receipt.updateUrlQrCode;
        this.notificationType = receipt.notificationType;
        this.notificationRecipient = receipt.notificationRecipient;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public List<NameValuePair> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NameValuePair> attributes) {
        this.attributes = attributes;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<NameValuePair> getSubjectDetails() {
        return subjectDetails;
    }

    public void setSubjectDetails(List<NameValuePair> subjectDetails) {
        this.subjectDetails = subjectDetails;
    }

    public Controller getDataController() {
        return dataController;
    }

    public void setDataController(Controller dataController) {
        this.dataController = dataController;
    }

    public String getHeaderNotice() {
        return headerNotice;
    }

    public void setHeaderNotice(String headerNotice) {
        this.headerNotice = headerNotice;
    }

    public List<Consent> getConsents() {
        return consents;
    }

    public void setConsents(List<Consent> consents) {
        this.consents = consents;
    }

    public String getFooterNotice() {
        return footerNotice;
    }

    public void setFooterNotice(String footerNotice) {
        this.footerNotice = footerNotice;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getUpdateUrlQrCode() {
        return updateUrlQrCode;
    }

    public void setUpdateUrlQrCode(String updateUrlQrCode) {
        this.updateUrlQrCode = updateUrlQrCode;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationRecipient() {
        return notificationRecipient;
    }

    public void setNotificationRecipient(String notificationRecipient) {
        this.notificationRecipient = notificationRecipient;
    }

    public static ConsentReceipt build(String transaction, String processor, ZonedDateTime date, ConsentContext ctx, BasicInfo info, List<Pair<ModelData, Record>> records) throws DatatypeConfigurationException {
        ConsentReceipt receipt = new ConsentReceipt();
        receipt.setTransaction(transaction);
        receipt.setLanguage(ctx.getLanguage());
        receipt.setDate(date);
        receipt.setExpirationDate(date.plus(ctx.getValidityInMillis(), ChronoUnit.MILLIS));
        receipt.setProcessor(processor);
        receipt.setSubject(ctx.getSubject());
        receipt.setSubjectDetails(ctx.getUserinfos().entrySet().stream().map(entry -> new NameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
        receipt.setAttributes(ctx.getAttributes().entrySet().stream().map(entry -> new NameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
        if (info != null) {
            receipt.setJurisdiction(info.getJurisdiction());
            receipt.setDataController(info.getDataController());
            receipt.setPrivacyPolicyUrl(info.getPrivacyPolicyUrl());
            receipt.setHeaderNotice(info.getTitle() + " " + info.getHeader());
            receipt.setFooterNotice(info.getFooter());
        }
        receipt.setCollectionMethod(ctx.getOrigin().toString());
        receipt.setConfirmation(ctx.getConfirmation().toString());

        for (Pair<ModelData, Record> pair : records) {
            if (Processing.TYPE.equals(pair.getLeft().getType())) {
                Processing entry = (Processing) pair.getLeft();
                ProcessingConsent consent = new ProcessingConsent();
                consent.setSerial(pair.getRight().serial);
                consent.setValue(pair.getRight().value);
                consent.setData(entry.getData());
                consent.setRetention(entry.getRetention());
                consent.setUsage(entry.getUsage());
                consent.setTitle(entry.getTitle());
                consent.setPurposes(entry.getPurposes().stream().map(Enum::name).collect(Collectors.toList()));
                consent.setController(entry.getDataController());
                consent.setContainsSensitiveData(entry.isContainsSensitiveData());
                consent.setContainsMedicalData(entry.isContainsMedicalData());
                if (entry.getThirdParties() != null && entry.getThirdParties().size() > 0) {
                    consent.setThirdParties(entry.getThirdParties());
                }
                receipt.getConsents().add(consent);
            }
            if (Preference.TYPE.equals(pair.getRight().type)) {
                Preference entry = (Preference) pair.getLeft();
                PreferenceConsent consent = new PreferenceConsent();
                consent.setSerial(pair.getRight().serial);
                consent.setValue(pair.getRight().value);
                consent.setLabel(entry.getLabel());
                consent.setDescription(entry.getDescription());
                receipt.getConsents().add(consent);
            }
            if (Conditions.TYPE.equals(pair.getRight().type)) {
                Conditions entry = (Conditions) pair.getLeft();
                ConditionsConsent consent = new ConditionsConsent();
                consent.setSerial(pair.getRight().serial);
                consent.setValue(pair.getRight().value);
                consent.setTitle(entry.getTitle());
                consent.setBody(entry.getBody());
                receipt.getConsents().add(consent);
            }
        }

        if (ctx.getNotificationRecipient() != null && !ctx.getNotificationRecipient().isEmpty()) {
            receipt.setNotificationType("email");
            receipt.setNotificationRecipient(ctx.getNotificationRecipient());
        }

        return receipt;
    }
}
