package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.common.util.ZonedDateTimeAdapter;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Record;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public String toXml() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Receipt.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(this, writer);
        writer.flush();
        return writer.toString();
    }

    public byte[] toXmlBytes() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Receipt.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(this, out);
        return out.toByteArray();
    }

    public static Receipt build(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Receipt.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Receipt) unmarshaller.unmarshal(new StringReader(xml));
    }

    public static Receipt build(String transaction, String processor, ZonedDateTime date, ConsentContext ctx, BasicInfo info, Map<Processing, Record> records) throws DatatypeConfigurationException {
        Receipt receipt = new Receipt();
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
        receipt.setCollectionMethod(ctx.getCollectionMethod());
        for ( Map.Entry<Processing, Record> record : records.entrySet() ) {
            Consent trecord = new Consent();
            trecord.setSerial(record.getValue().serial);
            trecord.setData(record.getKey().getData());
            trecord.setRetentionLabel(record.getKey().getRetentionLabel());
            trecord.setRetentionValue(record.getKey().getRetentionValue());
            trecord.setRetentionUnit(record.getKey().getRetentionUnit().name());
            trecord.setUsage(record.getKey().getUsage());
            trecord.setPurposes(record.getKey().getPurposes().stream().map(Enum::name).collect(Collectors.toList()));
            trecord.setController(record.getKey().getDataController());
            trecord.setValue(record.getValue().value);
            //TODO include specific processing sharing information
            receipt.getConsents().add(trecord);
        }
        return receipt;
    }

}
