package com.fairandsmart.consent.manager.data;

import com.fairandsmart.consent.manager.receipt.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Treatment extends ConsentElementData {

    public static final String TYPE = "treatment";

    // Main text
    public String treatmentTitle;
    public String dataTitle;
    public String dataBody;
    public String retentionTitle;
    public String retentionBody;
    public String usageTitle;
    public String usageBody;
    public List<Purpose> purposes;
    // Sensitive data
    public boolean containsSensitiveData = false;
    public boolean containsMedicalData = false;
    // Data controller
    private Controller dataController;
    private boolean showDataController = true;
    // Third parties (name: description)
    private Map<String, String> thirdParties;

    public Treatment() {
        this.setType(TYPE);
        this.setPurposes(new ArrayList<>());
        this.setThirdParties(new HashMap<>());
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

    public Map<String, String> getThirdParties() {
        return thirdParties;
    }

    public void setThirdParties(Map<String, String> thirdParties) {
        this.thirdParties = thirdParties;
    }

    public Treatment withThirdParties(Map<String, String> thirdParties) {
        this.thirdParties = thirdParties;
        return this;
    }

    public void addThirdParty(String thirdPartyName, String thirdPartyDescription) {
        this.thirdParties.put(thirdPartyName, thirdPartyDescription);
    }

    public Treatment withThirdParty(String thirdPartyName, String thirdPartyDescription) {
        this.thirdParties.put(thirdPartyName, thirdPartyDescription);
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
