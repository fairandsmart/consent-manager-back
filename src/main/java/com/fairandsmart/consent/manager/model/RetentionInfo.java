package com.fairandsmart.consent.manager.model;


public class RetentionInfo {

    public RetentionInfo() {}

    public RetentionInfo(String label, int value, RetentionUnit unit, String fullText) {
        this.label = label;
        this.value = value;
        this.unit = unit;
        this.fullText = fullText;
    }

    private String label;
    private int value;
    private RetentionUnit unit;
    private String fullText;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public RetentionUnit getUnit() {
        return unit;
    }

    public void setUnit(RetentionUnit unit) {
        this.unit = unit;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public enum RetentionUnit {
        YEAR,
        MONTH,
        WEEK
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetentionInfo that = (RetentionInfo) o;
        return value == that.value && label.equals(that.label) && unit == that.unit && fullText.equals(that.fullText);
    }


}
