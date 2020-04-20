package com.fairandsmart.consent.manager.data;

public class Treatment extends ModelData {

    public static final String TYPE = "treatment";

    public String data;
    public String retention;
    public String usage;
    public Purpose purpose;

    public Treatment() {
        this.setType(TYPE);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Treatment withData(String data) {
        this.data = data;
        return this;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public Treatment withRetention(String retention) {
        this.retention = retention;
        return this;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Treatment withUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Treatment withPurpose(Purpose purpose) {
        this.purpose = purpose;
        return this;
    }

    public enum Purpose {
        CONSENT_CORE_SERVICE,
        CONSENT_IMPROVED_SERVICE,
        CONSENT_MARKETING,
        CONSENT_THIRD_PART_SHARING,
        CONSENT_RESEARCH
    }

}
