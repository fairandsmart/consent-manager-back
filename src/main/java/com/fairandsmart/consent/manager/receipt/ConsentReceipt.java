package com.fairandsmart.consent.manager.receipt;

import java.util.List;

public class ConsentReceipt {

    private String transaction;
    private String jurisdiction;
    private String locale;
    private long timestamp;
    private String processor;
    private String subject;
    private List<NameValuePair> subjectDetails;
    private Controller dataController;
    private String headerNotice;
    private List<Record> records;
    private String footerNotice;
    private List<NameValuePair> attributes;
    private List<Attachment> attachments;
    private String privacyPolicyUrl;
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
}
