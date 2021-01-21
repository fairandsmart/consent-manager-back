package com.fairandsmart.consent.template;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
