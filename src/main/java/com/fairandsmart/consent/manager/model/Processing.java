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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Processing extends ModelData {

    public static final String TYPE = "processing";

    // Main text
    private String title;
    private String data;
    private RetentionInfo retention;
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
    // Associated preferences
    private boolean associatedWithPreferences = false;
    private List<String> associatedPreferences;

    public Processing() {
        this.setType(TYPE);
        this.setPurposes(new ArrayList<>());
        this.setThirdParties(new ArrayList<>());
        this.setAssociatedPreferences(new ArrayList<>());
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

    public boolean isAssociatedWithPreferences() {
        return associatedWithPreferences;
    }

    public void setAssociatedWithPreferences(boolean associatedWithPreferences) {
        this.associatedWithPreferences = associatedWithPreferences;
    }

    public Processing withAssociatedWithPreferences(boolean associatedWithPreferences) {
        this.associatedWithPreferences = associatedWithPreferences;
        return this;
    }

    public List<String> getAssociatedPreferences() {
        return associatedPreferences;
    }

    public void setAssociatedPreferences(List<String> associatedPreferences) {
        this.associatedPreferences = associatedPreferences;
    }

    public Processing withAssociatedPreferences(List<String> associatedPreferences) {
        this.associatedPreferences = associatedPreferences;
        return this;
    }

    public RetentionInfo getRetention() {
        return this.retention;
    }

    public void setRetention(RetentionInfo retention) {
        this.retention = retention;
    }

    public Processing withRetention(RetentionInfo retention) {
        this.retention = retention;
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
        Processing that = (Processing) o;
        return retention.equals(that.retention) &&
                containsSensitiveData == that.containsSensitiveData &&
                containsMedicalData == that.containsMedicalData &&
                showDataController == that.showDataController &&
                associatedWithPreferences == that.associatedWithPreferences &&
                Objects.equals(title, that.title) &&
                Objects.equals(data, that.data) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(purposes, that.purposes) &&
                Objects.equals(dataController, that.dataController) &&
                Objects.equals(thirdParties, that.thirdParties) &&
                Objects.equals(associatedPreferences, that.associatedPreferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, data, retention, usage, purposes, containsSensitiveData, containsMedicalData, dataController, showDataController, thirdParties, associatedWithPreferences, associatedPreferences);
    }

    @Override
    public String toString() {
        return "Processing{" +
                "title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", retention='" + retention + '\'' +
                ", usage='" + usage + '\'' +
                ", purposes=" + purposes +
                ", containsSensitiveData=" + containsSensitiveData +
                ", containsMedicalData=" + containsMedicalData +
                ", dataController=" + dataController +
                ", showDataController=" + showDataController +
                ", thirdParties=" + thirdParties +
                ", associatedWithPreferences=" + associatedWithPreferences +
                ", associatedPreferences=" + associatedPreferences +
                '}';
    }
}
