package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsentForm {

    private String token;
    private ModelVersion header;
    private List<ModelVersion> elements;
    private ModelVersion footer;
    private String locale;
    private Orientation orientation;
    private Map<String, String> previousValues;
    private boolean preview;
    private ModelVersion theme;

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

    public ModelVersion getHeader() {
        return header;
    }

    public void setHeader(ModelVersion header) {
        this.header = header;
    }

    public List<ModelVersion> getElements() {
        return elements;
    }

    public void setElements(List<ModelVersion> elements) {
        this.elements = elements;
    }

    public void addElement(ModelVersion element) {
        this.elements.add(element);
    }

    public ModelVersion getFooter() {
        return footer;
    }

    public void setFooter(ModelVersion footer) {
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

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public ModelVersion getTheme() {
        return theme;
    }

    public void setTheme(ModelVersion theme) {
        this.theme = theme;
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
                ", preview=" + preview +
                ", theme=" + theme +
                '}';
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
