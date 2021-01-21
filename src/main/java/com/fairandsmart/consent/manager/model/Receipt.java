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

import com.fairandsmart.consent.common.util.ZonedDateTimeAdapter;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Record;
import org.apache.commons.lang3.tuple.Pair;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Receipt {

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
    @XmlElement(name="consent")
    private List<Consent> consents;
    private String footerNotice;
    @XmlElementWrapper(name="attributes")
    @XmlElement(name="attribute")
    private List<NameValuePair> attributes;
    @XmlElementWrapper(name="attachments")
    @XmlElement(name="attachment")
    private List<Attachment> attachments;
    private String privacyPolicyUrl;
    private ConsentContext.CollectionMethod collectionMethod;
    private String updateUrl;
    private String updateUrlQrCode;
    private String notificationType;
    private String notificationRecipient;

    public Receipt() {
        subjectDetails = new ArrayList<>();
        consents = new ArrayList<>();
        attributes = new ArrayList<>();
        attachments = new ArrayList<>();
    }

    public Receipt(Receipt receipt) {
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

    public ConsentContext.CollectionMethod getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(ConsentContext.CollectionMethod collectionMethod) {
        this.collectionMethod = collectionMethod;
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

    public static Receipt build(String transaction, String processor, ZonedDateTime date, ConsentContext ctx, BasicInfo info, List<Pair<Processing, Record>> records) throws DatatypeConfigurationException {
        Receipt receipt = new Receipt();
        receipt.setTransaction(transaction);
        receipt.setLanguage(ctx.getLanguage());
        receipt.setDate(date);
        if (!ctx.isValidityVisible()) {
            receipt.setExpirationDate(null);
        } else {
            receipt.setExpirationDate(date.plus(ctx.getValidityInMillis(), ChronoUnit.MILLIS));
        }
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
        receipt.setCollectionMethod(ctx.getCollectionMethod());

        for (Pair<Processing, Record> pair : records) {
            Consent trecord = new Consent();
            trecord.setSerial(pair.getRight().serial);
            trecord.setData(pair.getLeft().getData());
            trecord.setRetention(pair.getLeft().getRetention());
            trecord.setUsage(pair.getLeft().getUsage());
            trecord.setTitle(pair.getLeft().getTitle());
            trecord.setPurposes(pair.getLeft().getPurposes().stream().map(Enum::name).collect(Collectors.toList()));
            trecord.setController(pair.getLeft().getDataController());
            trecord.setContainsSensitiveData(pair.getLeft().isContainsSensitiveData());
            trecord.setContainsMedicalData(pair.getLeft().isContainsMedicalData());
            trecord.setValue(pair.getRight().value);
            if (pair.getLeft().getThirdParties() != null && pair.getLeft().getThirdParties().size() > 0) {
                trecord.setThirdParties(pair.getLeft().getThirdParties());
            }
            receipt.getConsents().add(trecord);
        }

        if (ctx.getNotificationRecipient() != null && !ctx.getNotificationRecipient().isEmpty()) {
            receipt.setNotificationType("email");
            receipt.setNotificationRecipient(ctx.getNotificationRecipient());
        }

        return receipt;
    }
}
