package com.fairandsmart.consent.manager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Consent {

    private String serial;
    private String data;
    private String retention;
    private String usage;
    private Controller controller;
    @XmlElementWrapper(name="purposes")
    @XmlElement(name="purpose")
    private List<String> purposes;
    @XmlElementWrapper(name="sharings")
    @XmlElement(name="sharing")
    private List<Sharing> sharings;
    private String value;

    public Consent() {
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
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

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public List<Sharing> getSharings() {
        return sharings;
    }

    public void setSharings(List<Sharing> sharings) {
        this.sharings = sharings;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
