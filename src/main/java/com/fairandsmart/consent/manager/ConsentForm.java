package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsentForm {

    private String token;
    private ModelVersion info;
    private List<ModelVersion> elements;
    private String language;
    private Orientation orientation;
    private Map<String, String> previousValues;
    private boolean preview = false;
    private ModelVersion theme;
    private ModelVersion notificationEmail;

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

    public ModelVersion getInfo() {
        return info;
    }

    public void setInfo(ModelVersion info) {
        this.info = info;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public ModelVersion getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(ModelVersion notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    @Override
    public String toString() {
        return "ConsentForm{" +
                "token='" + token + '\'' +
                ", info=" + info +
                ", elements=" + elements +
                ", language='" + language + '\'' +
                ", orientation=" + orientation +
                ", preview=" + preview +
                ", theme=" + theme +
                ", notificationEmail=" + notificationEmail +
                '}';
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
