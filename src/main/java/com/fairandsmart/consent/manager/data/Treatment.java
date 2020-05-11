package com.fairandsmart.consent.manager.data;

public class Treatment extends ConsentElementData {

    public static final String TYPE = "treatment";

    public String treatmentTitle;
    public String dataTitle;
    public String dataBody;
    public String retentionTitle;
    public String retentionBody;
    public String usageTitle;
    public String usageBody;
    public Purpose purpose;

    public Treatment() {
        this.setType(TYPE);
    }

    public String getTreatmentTitle() {
        return treatmentTitle;
    }

    public void setTreatmentTitle(String treatmentTitle) {
        this.treatmentTitle = treatmentTitle;
    }

    public Treatment withTreatmentTitle(String treatmentTitle) {
        this.treatmentTitle = treatmentTitle;
        return this;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public Treatment withDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
        return this;
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }

    public Treatment withDataBody(String dataBody) {
        this.dataBody = dataBody;
        return this;
    }

    public String getRetentionTitle() {
        return retentionTitle;
    }

    public void setRetentionTitle(String retentionTitle) {
        this.retentionTitle = retentionTitle;
    }

    public Treatment withRetentionTitle(String retentionTitle) {
        this.retentionTitle = retentionTitle;
        return this;
    }

    public String getRetentionBody() {
        return retentionBody;
    }

    public void setRetentionBody(String retentionBody) {
        this.retentionBody = retentionBody;
    }

    public Treatment withRetentionBody(String retentionBody) {
        this.retentionBody = retentionBody;
        return this;
    }

    public String getUsageTitle() {
        return usageTitle;
    }

    public void setUsageTitle(String usageTitle) {
        this.usageTitle = usageTitle;
    }

    public Treatment withUsageTitle(String usageTitle) {
        this.usageTitle = usageTitle;
        return this;
    }

    public String getUsageBody() {
        return usageBody;
    }

    public void setUsageBody(String usageBody) {
        this.usageBody = usageBody;
    }

    public Treatment withUsageBody(String usageBody) {
        this.usageBody = usageBody;
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
