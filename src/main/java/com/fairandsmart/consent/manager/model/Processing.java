package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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

    private String title;
    private String data;
    private RetentionInfo retention;
    private String usage;
    private List<Purpose> purposes;
    private boolean containsSensitiveData = false;
    private boolean containsMedicalData = false;
    private Controller dataController;
    private boolean dataControllerVisible = true;
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

    public boolean isDataControllerVisible() {
        return dataControllerVisible;
    }

    public void setDataControllerVisible(boolean dataControllerVisible) {
        this.dataControllerVisible = dataControllerVisible;
    }

    public Processing withShowDataController(boolean showDataController) {
        this.dataControllerVisible = showDataController;
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
                dataControllerVisible == that.dataControllerVisible &&
                Objects.equals(title, that.title) &&
                Objects.equals(data, that.data) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(purposes, that.purposes) &&
                Objects.equals(dataController, that.dataController) &&
                Objects.equals(thirdParties, that.thirdParties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, data, retention, usage, purposes, containsSensitiveData, containsMedicalData, dataController, dataControllerVisible, thirdParties);
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
                ", showDataController=" + dataControllerVisible +
                ", thirdParties=" + thirdParties +
                '}';
    }
}
