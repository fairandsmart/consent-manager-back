package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Treatment extends ModelData {

    public static final String TYPE = "treatment";

    // Main text
    private String treatmentTitle;
    private String dataTitle;
    private String dataBody;
    private String retentionTitle;
    private String retentionBody;
    private String usageTitle;
    private String usageBody;
    private List<Purpose> purposes;
    // Sensitive data
    private boolean containsSensitiveData = false;
    private boolean containsMedicalData = false;
    // Data controller
    private Controller dataController;
    private boolean showDataController = true;
    // Third parties
    private List<NameValuePair> thirdParties;

    public Treatment() {
        this.setType(TYPE);
        this.setPurposes(new ArrayList<>());
        this.setThirdParties(new ArrayList<>());
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

    public List<Purpose> getPurposes() {
        return purposes;
    }

    public void setPurposes(List<Purpose> purposes) {
        this.purposes = purposes;
    }

    public void addPurpose(Purpose purpose) {
        this.purposes.add(purpose);
    }

    public Treatment withPurposes(List<Purpose> purposes) {
        this.purposes = purposes;
        return this;
    }

    public Treatment withPurpose(Purpose purpose) {
        this.purposes.add(purpose);
        return this;
    }

    public boolean isContainsSensitiveData() {
        return containsSensitiveData;
    }

    public void setContainsSensitiveData(boolean containsSensitiveData) {
        this.containsSensitiveData = containsSensitiveData;
    }

    public Treatment withContainsSensitiveData(boolean containsSensitiveData) {
        this.containsSensitiveData = containsSensitiveData;
        return this;
    }

    public boolean isContainsMedicalData() {
        return containsMedicalData;
    }

    public void setContainsMedicalData(boolean containsMedicalData) {
        this.containsMedicalData = containsMedicalData;
    }

    public Treatment withContainsMedicalData(boolean containsMedicalData) {
        this.containsMedicalData = containsMedicalData;
        return this;
    }

    public Controller getDataController() {
        return dataController;
    }

    public void setDataController(Controller dataController) {
        this.dataController = dataController;
    }

    public Treatment withDataController(Controller dataController) {
        this.dataController = dataController;
        return this;
    }

    public boolean isShowDataController() {
        return showDataController;
    }

    public void setShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
    }

    public Treatment withShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
        return this;
    }

    public List<NameValuePair> getThirdParties() {
        return thirdParties;
    }

    public void setThirdParties(List<NameValuePair> thirdParties) {
        this.thirdParties = thirdParties;
    }

    public Treatment withThirdParties(List<NameValuePair> thirdParties) {
        this.thirdParties = thirdParties;
        return this;
    }

    public void addThirdParty(NameValuePair thirdParty) {
        this.thirdParties.add(thirdParty);
    }

    public Treatment withThirdParty(NameValuePair thirdParty) {
        this.thirdParties.add(thirdParty);
        return this;
    }

    public enum Purpose {
        CONSENT_CORE_SERVICE,
        CONSENT_IMPROVED_SERVICE,
        CONSENT_MARKETING,
        CONSENT_THIRD_PART_SHARING,
        CONSENT_RESEARCH
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Treatment treatment = (Treatment) o;
        return containsSensitiveData == treatment.containsSensitiveData &&
                containsMedicalData == treatment.containsMedicalData &&
                showDataController == treatment.showDataController &&
                Objects.equals(treatmentTitle, treatment.treatmentTitle) &&
                Objects.equals(dataTitle, treatment.dataTitle) &&
                Objects.equals(dataBody, treatment.dataBody) &&
                Objects.equals(retentionTitle, treatment.retentionTitle) &&
                Objects.equals(retentionBody, treatment.retentionBody) &&
                Objects.equals(usageTitle, treatment.usageTitle) &&
                Objects.equals(usageBody, treatment.usageBody) &&
                Objects.equals(purposes, treatment.purposes) &&
                Objects.equals(dataController, treatment.dataController) &&
                Objects.equals(thirdParties, treatment.thirdParties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treatmentTitle, dataTitle, dataBody, retentionTitle, retentionBody, usageTitle, usageBody, purposes, containsSensitiveData, containsMedicalData, dataController, showDataController, thirdParties);
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "treatmentTitle='" + treatmentTitle + '\'' +
                ", dataTitle='" + dataTitle + '\'' +
                ", dataBody='" + dataBody + '\'' +
                ", retentionTitle='" + retentionTitle + '\'' +
                ", retentionBody='" + retentionBody + '\'' +
                ", usageTitle='" + usageTitle + '\'' +
                ", usageBody='" + usageBody + '\'' +
                ", purposes=" + purposes +
                ", containsSensitiveData=" + containsSensitiveData +
                ", containsMedicalData=" + containsMedicalData +
                ", dataController=" + dataController +
                ", showDataController=" + showDataController +
                ", thirdParties=" + thirdParties +
                '}';
    }
}
