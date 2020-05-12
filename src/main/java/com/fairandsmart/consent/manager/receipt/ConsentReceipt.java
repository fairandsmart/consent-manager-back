package com.fairandsmart.consent.manager.receipt;

import java.util.List;

public class ConsentReceipt {

    private String transaction;
    //TODO Include invalidated serials ?
    private String jurisdiction;
    private long timestamp;
    private String processor;
    private List<NameValuePair> attributes;
    private String subject;
    private List<NameValuePair> subjectDetails;
    private Controller dataController;
    private String headerNotice;
    private List<Record> records;
    private List<Sharing> sharings;
    private String footerNotice;
    private boolean containsSensitivePersonalInformation;
    private String privacyPolicyUrl;
    private List<Attachment> attachments;
    private String locale;
    //TODO Include modification url ?

    public ConsentReceipt() {
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

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<Sharing> getSharings() {
        return sharings;
    }

    public void setSharings(List<Sharing> sharings) {
        this.sharings = sharings;
    }

    public String getFooterNotice() {
        return footerNotice;
    }

    public void setFooterNotice(String footerNotice) {
        this.footerNotice = footerNotice;
    }

    public boolean isContainsSensitivePersonalInformation() {
        return containsSensitivePersonalInformation;
    }

    public void setContainsSensitivePersonalInformation(boolean containsSensitivePersonalInformation) {
        this.containsSensitivePersonalInformation = containsSensitivePersonalInformation;
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
}
