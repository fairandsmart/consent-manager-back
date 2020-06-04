package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Record;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Receipt {

    private String transaction;
    private String jurisdiction;
    private String locale;
    private long timestamp;
    private long expirationTimestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String toXml() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Receipt.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
        StringWriter writer = new StringWriter();
        marshaller.marshal(this, writer);
        writer.flush();
        return writer.toString();
    }

    public byte[] toXmlBytes() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Receipt.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(this, out);
        return out.toByteArray();
    }


    public static Receipt build(String transaction, String processor, long timestamp, ConsentContext ctx, Header header, Footer footer, Map<Treatment, Record> records) throws DatatypeConfigurationException {
        Receipt receipt = new Receipt();
        receipt.setTransaction(transaction);
        receipt.setLocale(ctx.getLocale());
        receipt.setTimestamp(timestamp);
        receipt.setExpirationTimestamp(receipt.getTimestamp() + ctx.getValidityInMillis());
        receipt.setProcessor(processor);
        receipt.setSubject(ctx.getSubject());
        receipt.setSubjectDetails(ctx.getUserinfos().entrySet().stream().map(entry -> new NameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
        receipt.setAttributes(ctx.getAttributes().entrySet().stream().map(entry -> new NameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
        receipt.setJurisdiction(header.getJurisdiction());
        receipt.setDataController(header.getDataController());
        receipt.setPrivacyPolicyUrl(header.getPrivacyPolicyUrl());
        receipt.setHeaderNotice(header.getTitle() + " " + header.getBody());
        receipt.setFooterNotice(footer.getBody());
        for ( Map.Entry<Treatment, Record> record : records.entrySet() ) {
            Consent trecord = new Consent();
            trecord.setSerial(record.getValue().serial);
            trecord.setData(record.getKey().getDataBody());
            trecord.setRetention(record.getKey().getRetentionBody());
            trecord.setUsage(record.getKey().getUsageBody());
            trecord.setPurposes(record.getKey().getPurposes().stream().map(p -> p.name()).collect(Collectors.toList()));
            trecord.setController(record.getKey().getDataController());
            trecord.setValue(record.getValue().value);
            //TODO include specific treatment sharing information
            receipt.getConsents().add(trecord);
        }
        return receipt;
    }

}
