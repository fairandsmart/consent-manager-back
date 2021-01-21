package com.fairandsmart.consent.template;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import java.util.ResourceBundle;

public class TemplateModel<T> {

    private String template;
    private String language;
    private ResourceBundle bundle;
    private T data;

    public TemplateModel() {
    }

    public TemplateModel(String templateName, T data, String language) {
        this.template = templateName;
        this.language = language;
        this.data = data;
    }

    public TemplateModel(String templateName, T data, String language, ResourceBundle bundle) {
        this.template = templateName;
        this.language = language;
        this.data = data;
        this.bundle = bundle;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
                ", language=" + language +
                ", bundle=" + bundle +
                ", data=" + data +
                '}';
    }
}
