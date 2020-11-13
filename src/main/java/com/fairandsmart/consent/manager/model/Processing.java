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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Processing extends ModelData {

    public static final String TYPE = "processing";

    // Main text
    private String title;
    private String data;
    private String retention;
    private int retentionDuration;
    private RetentionUnit retentionUnit;
    private String usage;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Processing withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Processing withData(String data) {
        this.data = data;
        return this;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public Processing withRetention(String retention) {
        this.retention = retention;
        return this;
    }

    public int getRetentionDuration() {
        return retentionDuration;
    }

    public void setRetentionDuration(int retentionDuration) {
        this.retentionDuration = retentionDuration;
    }

    public Processing withRetentionDuration(int retentionDuration) {
        this.retentionDuration = retentionDuration;
        return this;
    }

    public RetentionUnit getRetentionUnit() {
        return retentionUnit;
    }

    public void setRetentionUnit(RetentionUnit retentionUnit) {
        this.retentionUnit = retentionUnit;
    }

    public Processing withRetentionUnit(RetentionUnit retentionUnit) {
        this.retentionUnit = retentionUnit;
        return this;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Processing withUsage(String usage) {
        this.usage = usage;
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

    public enum RetentionUnit {
        YEAR,
        MONTH,
        WEEK
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
        Processing that = (Processing) o;
        return retentionDuration == that.retentionDuration &&
                containsSensitiveData == that.containsSensitiveData &&
                containsMedicalData == that.containsMedicalData &&
                showDataController == that.showDataController &&
                Objects.equals(title, that.title) &&
                Objects.equals(data, that.data) &&
                Objects.equals(retention, that.retention) &&
                retentionUnit == that.retentionUnit &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(purposes, that.purposes) &&
                Objects.equals(dataController, that.dataController) &&
                Objects.equals(thirdParties, that.thirdParties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, data, retention, retentionDuration, retentionUnit, usage, purposes, containsSensitiveData, containsMedicalData, dataController, showDataController, thirdParties);
    }

    @Override
    public String toString() {
        return "Processing{" +
                "title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", retention='" + retention + '\'' +
                ", retentionDuration=" + retentionDuration +
                ", retentionUnit=" + retentionUnit +
                ", usage='" + usage + '\'' +
                ", purposes=" + purposes +
                ", containsSensitiveData=" + containsSensitiveData +
                ", containsMedicalData=" + containsMedicalData +
                ", dataController=" + dataController +
                ", showDataController=" + showDataController +
                ", thirdParties=" + thirdParties +
                '}';
    }
}
