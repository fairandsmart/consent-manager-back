package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.List;
import java.util.Objects;

public class Preference extends ModelData {

    public static final String TYPE = "preference";

    private String label;
    private boolean associatedWithProcessing;
    private List<String> associatedProcessing;
    private String description;
    private List<String> options;
    private ValueType valueType;

    public Preference() {
        this.setType(TYPE);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAssociatedWithProcessing() {
        return associatedWithProcessing;
    }

    public void setAssociatedWithProcessing(boolean associatedWithProcessing) {
        this.associatedWithProcessing = associatedWithProcessing;
    }

    public List<String> getAssociatedProcessing() {
        return associatedProcessing;
    }

    public void setAssociatedProcessing(List<String> associatedProcessing) {
        this.associatedProcessing = associatedProcessing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preference that = (Preference) o;
        return associatedWithProcessing == that.associatedWithProcessing &&
                Objects.equals(label, that.label) &&
                Objects.equals(associatedProcessing, that.associatedProcessing) &&
                Objects.equals(description, that.description) &&
                Objects.equals(options, that.options) &&
                valueType == that.valueType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, associatedWithProcessing, associatedProcessing, description, options, valueType);
    }

    @Override
    public String toString() {
        return "Preference{" +
                "label='" + label + '\'' +
                ", associatedWithProcessing=" + associatedWithProcessing +
                ", associatedProcessing=" + associatedProcessing +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", valueType=" + valueType +
                '}';
    }
}
