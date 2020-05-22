package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.ConsentElementVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsentForm {

    private String token;
    private ConsentElementVersion header;
    private List<ConsentElementVersion> elements;
    private ConsentElementVersion footer;
    private String locale;
    private Orientation orientation;
    private Map<String, String> previousValues;

    public ConsentForm() {
        elements = new ArrayList<>();
        previousValues = new HashMap<>();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ConsentElementVersion getHeader() {
        return header;
    }

    public void setHeader(ConsentElementVersion header) {
        this.header = header;
    }

    public List<ConsentElementVersion> getElements() {
        return elements;
    }

    public void setElements(List<ConsentElementVersion> elements) {
        this.elements = elements;
    }

    public void addElement(ConsentElementVersion element) {
        this.elements.add(element);
    }

    public ConsentElementVersion getFooter() {
        return footer;
    }

    public void setFooter(ConsentElementVersion footer) {
        this.footer = footer;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Map<String, String> getPreviousValues() {
        return previousValues;
    }

    public void setPreviousValues(Map<String, String> previousValues) {
        this.previousValues = previousValues;
    }

    public void addPreviousValue(String key, String value) {
        this.previousValues.put(key, value);
    }

    @Override
    public String toString() {
        return "ConsentForm{" +
                "token='" + token + '\'' +
                ", header=" + header +
                ", elements=" + elements +
                ", footer=" + footer +
                ", locale='" + locale + '\'' +
                ", orientation=" + orientation +
                '}';
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
