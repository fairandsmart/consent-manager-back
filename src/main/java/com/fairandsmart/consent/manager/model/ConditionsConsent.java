package com.fairandsmart.consent.manager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionsConsent extends Consent {

    public static final String TYPE = Conditions.TYPE;

    private String title;
    private String body;

    public ConditionsConsent() {
        this.setType(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ConditionsConsent{" +
                "serial='" + getSerial() + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", value='" + getValue() + '\'' +
                '}';
    }
}
