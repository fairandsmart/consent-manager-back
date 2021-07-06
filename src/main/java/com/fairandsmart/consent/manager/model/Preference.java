package com.fairandsmart.consent.manager.model;

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

import com.fairandsmart.consent.manager.entity.ModelData;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Preference extends ModelData {

    public static final String TYPE = "preference";

    private String label;
    private String description;
    private List<String> options;
    private ValueType valueType;
    private boolean includeDefault;
    private List<String> defaultValues;
    private List<String> valuePatterns;
    private boolean optional;

    public Preference() {
        this.setType(TYPE);
        this.options = new ArrayList<>();
        this.defaultValues = new ArrayList<>();
        this.valuePatterns = new ArrayList<>();
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

    public List<String> getValuePatterns() {
        return valuePatterns;
    }

    public void setValuePatterns(List<String> valuePatterns) {
        this.valuePatterns = valuePatterns;
    }

    public Preference withValuePatterns(List<String> valuePatterns) {
        this.valuePatterns = valuePatterns;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public Preference withOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public enum ValueType {
        TOGGLE,
        CHECKBOXES,
        RADIO_BUTTONS,
        LIST_SINGLE,
        LIST_MULTI,
        FREE_TEXT
    }

    @Override
    public List<Pattern> getAllowedValuesPatterns() {
        return valuePatterns.stream().map(Pattern::compile).collect(Collectors.toList());
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
        return includeDefault == that.includeDefault && optional == that.optional && Objects.equals(label, that.label) && Objects.equals(description, that.description) && Objects.equals(options, that.options) && valueType == that.valueType && Objects.equals(defaultValues, that.defaultValues) && Objects.equals(valuePatterns, that.valuePatterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, description, options, valueType, includeDefault, defaultValues, valuePatterns, optional);
    }

    @Override
    public String toString() {
        return "Preference{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", valueType=" + valueType +
                ", includeDefault=" + includeDefault +
                ", defaultValues=" + defaultValues +
                ", valuePatterns=" + valuePatterns +
                ", optional=" + optional +
                '}';
    }
}
