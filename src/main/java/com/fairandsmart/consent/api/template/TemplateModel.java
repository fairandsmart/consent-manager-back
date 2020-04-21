package com.fairandsmart.consent.api.template;

import java.util.Locale;
import java.util.ResourceBundle;

public class TemplateModel<T> {

    private String templateName;
    private Locale locale;
    private ResourceBundle bundle;
    private T data;

    public TemplateModel() {
    }

    public TemplateModel(String templateName, T data, Locale locale) {
        this.templateName = templateName;
        this.locale = locale;
        this.data = data;
    }

    public TemplateModel(String name, T data, Locale locale, ResourceBundle bundle) {
        this.templateName = templateName;
        this.locale = locale;
        this.data = data;
        this.bundle = bundle;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public String toString() {
        return "TemplateModel{" +
                "templateName='" + templateName + '\'' +
                ", locale=" + locale +
                ", bundle=" + bundle +
                ", data=" + data +
                '}';
    }
}
