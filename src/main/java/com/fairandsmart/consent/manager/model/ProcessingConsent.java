package com.fairandsmart.consent.manager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessingConsent extends Consent {

    public static final String TYPE = Processing.TYPE;

    private String title;
    private String data;
    private RetentionInfo retention;
    private String usage;
    @XmlElementWrapper(name="purposes")
    @XmlElement(name="purpose")
    private List<String> purposes;
    private boolean containsSensitiveData;
    private boolean containsMedicalData;
    private Controller controller;
    @XmlElementWrapper(name="thirdParties")
    @XmlElement(name="thirdParty")
    private List<NameValuePair> thirdParties;

    public ProcessingConsent() {
        this.setType(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public RetentionInfo getRetention() {
        return retention;
    }

    public void setRetention(RetentionInfo retention) {
        this.retention = retention;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public List<String> getPurposes() {
        return purposes;
    }

    public void setPurposes(List<String> purposes) {
        this.purposes = purposes;
    }

    public boolean isContainsSensitiveData() {
        return containsSensitiveData;
    }

    public void setContainsSensitiveData(boolean containsSensitiveData) {
        this.containsSensitiveData = containsSensitiveData;
    }

    public boolean isContainsMedicalData() {
        return containsMedicalData;
    }

    public void setContainsMedicalData(boolean containsMedicalData) {
        this.containsMedicalData = containsMedicalData;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public List<NameValuePair> getThirdParties() {
        return thirdParties;
    }

    public void setThirdParties(List<NameValuePair> thirdParties) {
        this.thirdParties = thirdParties;
    }

    @Override
    public String toString() {
        return "ProcessingConsent{" +
                "serial='" + getSerial() + '\'' +
                ", title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", retention=" + retention +
                ", usage='" + usage + '\'' +
                ", purposes=" + purposes +
                ", containsSensitiveData=" + containsSensitiveData +
                ", containsMedicalData=" + containsMedicalData +
                ", controller=" + controller +
                ", thirdParties=" + thirdParties +
                ", value='" + getValue() + '\'' +
                '}';
    }
}
