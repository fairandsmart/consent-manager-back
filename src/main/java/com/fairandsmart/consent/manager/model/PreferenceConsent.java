package com.fairandsmart.consent.manager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class PreferenceConsent extends Consent {

    public static final String TYPE = Preference.TYPE;

    private String label;
    private String description;

    public PreferenceConsent() {
        this.setType(TYPE);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PreferenceConsent{" +
                "serial='" + getSerial() + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", value='" + getValue() + '\'' +
                '}';
    }
}
