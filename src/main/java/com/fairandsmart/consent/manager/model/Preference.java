package com.fairandsmart.consent.manager.model;

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

import com.fairandsmart.consent.manager.entity.ModelData;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Preference extends ModelData {

    public static final String TYPE = "preference";

    private String label;
    private String description;
    private List<String> options;
    private ValueType valueType;
    private boolean includeDefault;
    private List<String> defaultValues;

    public Preference() {
        this.setType(TYPE);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Preference withLabel(String label) {
        this.label = label;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Preference withDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Preference withOptions(List<String> options) {
        this.options = options;
        return this;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public Preference withValueType(ValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    public boolean isIncludeDefault() {
        return includeDefault;
    }

    public void setIncludeDefault(boolean includeDefault) {
        this.includeDefault = includeDefault;
    }

    public Preference withIncludeDefault(boolean includeDefault) {
        this.includeDefault = includeDefault;
        return this;
    }

    public List<String> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(List<String> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public Preference withDefaultValues(List<String> defaultValues) {
        this.defaultValues = defaultValues;
        return this;
    }

    public enum ValueType {
        NONE,
        TOGGLE,
        CHECKBOXES,
        RADIO_BUTTONS,
        LIST_SINGLE,
        LIST_MULTI,
        FREE_TEXT
    }

    @Override
    public String extractDataMimeType() {
        return MediaType.APPLICATION_JSON;
    }

    @Override
    public String toMimeContent() throws IOException {
        return this.toJson();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preference that = (Preference) o;
        return includeDefault == that.includeDefault &&
                Objects.equals(label, that.label) &&
                Objects.equals(description, that.description) &&
                Objects.equals(options, that.options) &&
                valueType == that.valueType &&
                Objects.equals(defaultValues, that.defaultValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, description, options, valueType, includeDefault, defaultValues);
    }

    @Override
    public String toString() {
        return "Preference{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", valueType=" + valueType +
                ", includeDefault=" + includeDefault +
                ", defaultValues='" + defaultValues + '\'' +
                '}';
    }
}
