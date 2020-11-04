package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.manager.entity.ModelVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsentForm {

    private String token;
    private ModelVersion info;
    private List<ModelVersion> elements;
    private String locale;
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
                ", locale='" + locale + '\'' +
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
