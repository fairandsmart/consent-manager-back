package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consent / A Consent Manager Plateform
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Processing extends ModelData {

    public static final String TYPE = "processing";

    // Main text
    private String processingTitle;
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

    public Processing() {
        this.setType(TYPE);
        this.setPurposes(new ArrayList<>());
        this.setThirdParties(new ArrayList<>());
    }

    public String getProcessingTitle() {
        return processingTitle;
    }

    public void setProcessingTitle(String processingTitle) {
        this.processingTitle = processingTitle;
    }

    public Processing withProcessingTitle(String processingTitle) {
        this.processingTitle = processingTitle;
        return this;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public Processing withDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
        return this;
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }

    public Processing withDataBody(String dataBody) {
        this.dataBody = dataBody;
        return this;
    }

    public String getRetentionTitle() {
        return retentionTitle;
    }

    public void setRetentionTitle(String retentionTitle) {
        this.retentionTitle = retentionTitle;
    }

    public Processing withRetentionTitle(String retentionTitle) {
        this.retentionTitle = retentionTitle;
        return this;
    }

    public String getRetentionBody() {
        return retentionBody;
    }

    public void setRetentionBody(String retentionBody) {
        this.retentionBody = retentionBody;
    }

    public Processing withRetentionBody(String retentionBody) {
        this.retentionBody = retentionBody;
        return this;
    }

    public String getUsageTitle() {
        return usageTitle;
    }

    public void setUsageTitle(String usageTitle) {
        this.usageTitle = usageTitle;
    }

    public Processing withUsageTitle(String usageTitle) {
        this.usageTitle = usageTitle;
        return this;
    }

    public String getUsageBody() {
        return usageBody;
    }

    public void setUsageBody(String usageBody) {
        this.usageBody = usageBody;
    }

    public Processing withUsageBody(String usageBody) {
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

    public Processing withPurposes(List<Purpose> purposes) {
        this.purposes = purposes;
        return this;
    }

    public Processing withPurpose(Purpose purpose) {
        this.purposes.add(purpose);
        return this;
    }

    public boolean isContainsSensitiveData() {
        return containsSensitiveData;
    }

    public void setContainsSensitiveData(boolean containsSensitiveData) {
        this.containsSensitiveData = containsSensitiveData;
    }

    public Processing withContainsSensitiveData(boolean containsSensitiveData) {
        this.containsSensitiveData = containsSensitiveData;
        return this;
    }

    public boolean isContainsMedicalData() {
        return containsMedicalData;
    }

    public void setContainsMedicalData(boolean containsMedicalData) {
        this.containsMedicalData = containsMedicalData;
    }

    public Processing withContainsMedicalData(boolean containsMedicalData) {
        this.containsMedicalData = containsMedicalData;
        return this;
    }

    public Controller getDataController() {
        return dataController;
    }

    public void setDataController(Controller dataController) {
        this.dataController = dataController;
    }

    public Processing withDataController(Controller dataController) {
        this.dataController = dataController;
        return this;
    }

    public boolean isShowDataController() {
        return showDataController;
    }

    public void setShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
    }

    public Processing withShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
        return this;
    }

    public List<NameValuePair> getThirdParties() {
        return thirdParties;
    }

    public void setThirdParties(List<NameValuePair> thirdParties) {
        this.thirdParties = thirdParties;
    }

    public Processing withThirdParties(List<NameValuePair> thirdParties) {
        this.thirdParties = thirdParties;
        return this;
    }

    public void addThirdParty(NameValuePair thirdParty) {
        this.thirdParties.add(thirdParty);
    }

    public Processing withThirdParty(NameValuePair thirdParty) {
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
        Processing processing = (Processing) o;
        return containsSensitiveData == processing.containsSensitiveData &&
                containsMedicalData == processing.containsMedicalData &&
                showDataController == processing.showDataController &&
                Objects.equals(processingTitle, processing.processingTitle) &&
                Objects.equals(dataTitle, processing.dataTitle) &&
                Objects.equals(dataBody, processing.dataBody) &&
                Objects.equals(retentionTitle, processing.retentionTitle) &&
                Objects.equals(retentionBody, processing.retentionBody) &&
                Objects.equals(usageTitle, processing.usageTitle) &&
                Objects.equals(usageBody, processing.usageBody) &&
                Objects.equals(purposes, processing.purposes) &&
                Objects.equals(dataController, processing.dataController) &&
                Objects.equals(thirdParties, processing.thirdParties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processingTitle, dataTitle, dataBody, retentionTitle, retentionBody, usageTitle, usageBody, purposes, containsSensitiveData, containsMedicalData, dataController, showDataController, thirdParties);
    }

    @Override
    public String toString() {
        return "Processing{" +
                "processingTitle='" + processingTitle + '\'' +
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
