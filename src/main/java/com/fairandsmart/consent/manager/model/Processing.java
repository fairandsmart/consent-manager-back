package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.manager.entity.ModelData;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Processing extends ModelData {

    public static final String TYPE = "processing";
    public static final List<Pattern> ALLOWED_VALUES_PATTERNS = new ArrayList<>();
    static {
        ALLOWED_VALUES_PATTERNS.clear();
        ALLOWED_VALUES_PATTERNS.add(Pattern.compile("accepted"));
        ALLOWED_VALUES_PATTERNS.add(Pattern.compile("refused"));
    }

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
    public List<Pattern> getAllowedValuesPatterns() {
        return ALLOWED_VALUES_PATTERNS;
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
