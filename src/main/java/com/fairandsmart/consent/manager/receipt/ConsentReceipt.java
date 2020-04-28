package com.fairandsmart.consent.manager.receipt;

import java.util.List;

public class ConsentReceipt {

    public String id;
    public String transaction;
    //TODO Include invalidated serials ?
    public String jurisdiction;
    public String timestamp;
    public String processor;
    public List<NameValuePair> attributes;
    public String subject;
    public List<NameValuePair> subjectDetails;
    public Controller dataController;
    public String headerNotice;
    public List<Record> records;
    public List<Sharing> sharings;
    public String footerNotice;
    public boolean containsSensitivePersonalInformation;
    public String privacyPolicyUrl;
    public List<Attachment> attachments;
    //TODO Include modification url ?


}
