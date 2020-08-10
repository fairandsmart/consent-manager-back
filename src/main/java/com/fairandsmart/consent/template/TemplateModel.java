package com.fairandsmart.consent.template;

import java.util.Locale;
import java.util.ResourceBundle;

public class TemplateModel<T> {

    private String template;
    private Locale locale;
    private ResourceBundle bundle;
    private T data;

    public TemplateModel() {
    }

    public TemplateModel(String templateName, T data, Locale locale) {
        this.template = templateName;
        this.locale = locale;
        this.data = data;
    }

    public TemplateModel(String templateName, T data, Locale locale, ResourceBundle bundle) {
        this.template = templateName;
        this.locale = locale;
        this.data = data;
        this.bundle = bundle;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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
                "template='" + template + '\'' +
                ", locale=" + locale +
                ", bundle=" + bundle +
                ", data=" + data +
                '}';
    }
}
