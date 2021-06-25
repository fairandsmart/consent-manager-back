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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class BasicInfo extends ModelData {

    public static final String TYPE = "basicinfo";

    // Main text
    private String title;
    private String header;
    private String footer;
    // Consent parameters
    private String jurisdiction;
    private boolean jurisdictionVisible = false;
    private Controller dataController;
    private boolean dataControllerVisible = false;
    private String scope;
    private boolean scopeVisible = false;
    private String shortNoticeLink;
    private boolean shortNoticeLinkVisible = false;
    // Privacy policy
    private String privacyPolicyUrl;
    private String customPrivacyPolicyText;

    public BasicInfo() {
        this.setType(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BasicInfo withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public BasicInfo withHeader(String header) {
        this.header = header;
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public BasicInfo withFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public BasicInfo withJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
        return this;
    }

    public boolean isJurisdictionVisible() {
        return jurisdictionVisible;
    }

    public void setJurisdictionVisible(boolean jurisdictionVisible) {
        this.jurisdictionVisible = jurisdictionVisible;
    }

    public BasicInfo withShowJurisdiction(boolean showJurisdiction) {
        this.jurisdictionVisible = showJurisdiction;
        return this;
    }

    public Controller getDataController() {
        return dataController;
    }

    public void setDataController(Controller dataController) {
        this.dataController = dataController;
    }

    public BasicInfo withDataController(Controller dataController) {
        this.dataController = dataController;
        return this;
    }

    public boolean isDataControllerVisible() {
        return dataControllerVisible;
    }

    public void setDataControllerVisible(boolean dataControllerVisible) {
        this.dataControllerVisible = dataControllerVisible;
    }

    public BasicInfo withShowDataController(boolean showDataController) {
        this.dataControllerVisible = showDataController;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public BasicInfo withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public boolean isScopeVisible() {
        return scopeVisible;
    }

    public void setScopeVisible(boolean scopeVisible) {
        this.scopeVisible = scopeVisible;
    }

    public BasicInfo withShowScope(boolean showScope) {
        this.scopeVisible = showScope;
        return this;
    }

    public String getShortNoticeLink() {
        return shortNoticeLink;
    }

    public void setShortNoticeLink(String shortNoticeLink) {
        this.shortNoticeLink = shortNoticeLink;
    }

    public BasicInfo withShortNoticeLink(String shortNoticeLink) {
        this.shortNoticeLink = shortNoticeLink;
        return this;
    }

    public boolean isShortNoticeLinkVisible() {
        return shortNoticeLinkVisible;
    }

    public void setShortNoticeLinkVisible(boolean shortNoticeLinkVisible) {
        this.shortNoticeLinkVisible = shortNoticeLinkVisible;
    }

    public BasicInfo withShowShortNoticeLink(boolean showShortNoticeLink) {
        this.shortNoticeLinkVisible = showShortNoticeLink;
        return this;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

    public BasicInfo withPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
        return this;
    }

    public String getCustomPrivacyPolicyText() {
        return customPrivacyPolicyText;
    }

    public void setCustomPrivacyPolicyText(String customPrivacyPolicyText) {
        this.customPrivacyPolicyText = customPrivacyPolicyText;
    }

    public BasicInfo withCustomPrivacyPolicyText(String customPrivacyPolicyText) {
        this.customPrivacyPolicyText = customPrivacyPolicyText;
        return this;
    }

    @Override
    public List<Pattern> getAllowedValuesPatterns() {
        return Collections.emptyList();
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
        BasicInfo info = (BasicInfo) o;
        return jurisdictionVisible == info.jurisdictionVisible &&
                dataControllerVisible == info.dataControllerVisible &&
                scopeVisible == info.scopeVisible &&
                shortNoticeLinkVisible == info.shortNoticeLinkVisible &&
                Objects.equals(title, info.title) &&
                Objects.equals(header, info.header) &&
                Objects.equals(footer, info.footer) &&
                Objects.equals(jurisdiction, info.jurisdiction) &&
                Objects.equals(dataController, info.dataController) &&
                Objects.equals(scope, info.scope) &&
                Objects.equals(shortNoticeLink, info.shortNoticeLink) &&
                Objects.equals(privacyPolicyUrl, info.privacyPolicyUrl) &&
                Objects.equals(customPrivacyPolicyText, info.customPrivacyPolicyText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, header, footer, jurisdiction, jurisdictionVisible, dataController, dataControllerVisible, scope, scopeVisible, shortNoticeLink, shortNoticeLinkVisible, privacyPolicyUrl, customPrivacyPolicyText);
    }

    @Override
    public String toString() {
        return "BasicInfo{" +
                "title='" + title + '\'' +
                ", header='" + header + '\'' +
                ", footer='" + footer + '\'' +
                ", jurisdiction='" + jurisdiction + '\'' +
                ", jurisdictionVisible=" + jurisdictionVisible +
                ", dataController=" + dataController +
                ", dataControllerVisible=" + dataControllerVisible +
                ", scope='" + scope + '\'' +
                ", scopeVisible=" + scopeVisible +
                ", shortNoticeLink='" + shortNoticeLink + '\'' +
                ", shortNoticeLinkVisible=" + shortNoticeLinkVisible +
                ", privacyPolicyUrl='" + privacyPolicyUrl + '\'' +
                ", customPrivacyPolicyText='" + customPrivacyPolicyText + '\'' +
                '}';
    }
}
