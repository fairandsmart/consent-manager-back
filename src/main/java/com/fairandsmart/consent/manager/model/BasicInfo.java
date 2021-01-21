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
import java.util.Objects;

public class BasicInfo extends ModelData {

    public static final String TYPE = "basicinfo";

    // Main text
    private String title;
    private String header;
    private String footer;
    // Consent parameters
    private String jurisdiction;
    private boolean jurisdictionVisible = false;
    private String collectionMethod;
    private boolean collectionMethodVisible = false;
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

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public BasicInfo withCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
        return this;
    }

    public boolean isCollectionMethodVisible() {
        return collectionMethodVisible;
    }

    public void setCollectionMethodVisible(boolean collectionMethodVisible) {
        this.collectionMethodVisible = collectionMethodVisible;
    }

    public BasicInfo withShowCollectionMethod(boolean showCollectionMethod) {
        this.collectionMethodVisible = showCollectionMethod;
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
                collectionMethodVisible == info.collectionMethodVisible &&
                dataControllerVisible == info.dataControllerVisible &&
                scopeVisible == info.scopeVisible &&
                shortNoticeLinkVisible == info.shortNoticeLinkVisible &&
                Objects.equals(title, info.title) &&
                Objects.equals(header, info.header) &&
                Objects.equals(footer, info.footer) &&
                Objects.equals(jurisdiction, info.jurisdiction) &&
                Objects.equals(collectionMethod, info.collectionMethod) &&
                Objects.equals(dataController, info.dataController) &&
                Objects.equals(scope, info.scope) &&
                Objects.equals(shortNoticeLink, info.shortNoticeLink) &&
                Objects.equals(privacyPolicyUrl, info.privacyPolicyUrl) &&
                Objects.equals(customPrivacyPolicyText, info.customPrivacyPolicyText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, header, footer, jurisdiction, jurisdictionVisible, collectionMethod, collectionMethodVisible, dataController, dataControllerVisible, scope, scopeVisible, shortNoticeLink, shortNoticeLinkVisible, privacyPolicyUrl, customPrivacyPolicyText);
    }

    @Override
    public String toString() {
        return "BasicInfo{" +
                "title='" + title + '\'' +
                ", header='" + header + '\'' +
                ", footer='" + footer + '\'' +
                ", jurisdiction='" + jurisdiction + '\'' +
                ", jurisdictionVisible=" + jurisdictionVisible +
                ", collectionMethod='" + collectionMethod + '\'' +
                ", collectionMethodVisible=" + collectionMethodVisible +
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
