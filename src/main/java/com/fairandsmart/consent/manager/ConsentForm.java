package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import java.util.ArrayList;
import java.util.List;

public class ConsentForm {

    private String token;
    private ModelVersion header;
    private List<ModelVersion> elements;
    private ModelVersion footer;
    private String locale;
    private Orientation orientation;

    public ConsentForm() {
        elements = new ArrayList<>();
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
