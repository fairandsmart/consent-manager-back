package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.Objects;

public class BasicInfo extends ModelData {

    public static final String TYPE = "basicinfo";

    // Logo
    private String logoPath;
    private String logoAltText = "Logo";
    // Main text
    private String title;
    private String header;
    private String footer;
    // Consent parameters
    private String jurisdiction;
    private boolean showJurisdiction = false;
    private String collectionMethod;
    private boolean showCollectionMethod = false;
    private Controller dataController;
    private boolean showDataController = false;
    private String scope;
    private boolean showScope = false;
    private String shortNoticeLink;
    private boolean showShortNoticeLink = false;
    // Privacy policy
    private String privacyPolicyUrl;
    private String customPrivacyPolicyText;
    // Validation options
    private boolean showAcceptAll = false;
    private String customAcceptAllText;

    public BasicInfo() {
        this.setType(TYPE);
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public BasicInfo withLogoPath(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

    public String getLogoAltText() {
        return logoAltText;
    }

    public void setLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
    }

    public BasicInfo withLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
        return this;
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

    public boolean isShowJurisdiction() {
        return showJurisdiction;
    }

    public void setShowJurisdiction(boolean showJurisdiction) {
        this.showJurisdiction = showJurisdiction;
    }

    public BasicInfo withShowJurisdiction(boolean showJurisdiction) {
        this.showJurisdiction = showJurisdiction;
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

    public boolean isShowCollectionMethod() {
        return showCollectionMethod;
    }

    public void setShowCollectionMethod(boolean showCollectionMethod) {
        this.showCollectionMethod = showCollectionMethod;
    }

    public BasicInfo withShowCollectionMethod(boolean showCollectionMethod) {
        this.showCollectionMethod = showCollectionMethod;
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

    public boolean isShowDataController() {
        return showDataController;
    }

    public void setShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
    }

    public BasicInfo withShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
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

    public boolean isShowScope() {
        return showScope;
    }

    public void setShowScope(boolean showScope) {
        this.showScope = showScope;
    }

    public BasicInfo withShowScope(boolean showScope) {
        this.showScope = showScope;
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

    public boolean isShowShortNoticeLink() {
        return showShortNoticeLink;
    }

    public void setShowShortNoticeLink(boolean showShortNoticeLink) {
        this.showShortNoticeLink = showShortNoticeLink;
    }

    public BasicInfo withShowShortNoticeLink(boolean showShortNoticeLink) {
        this.showShortNoticeLink = showShortNoticeLink;
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

    public boolean isShowAcceptAll() {
        return showAcceptAll;
    }

    public void setShowAcceptAll(boolean showAcceptAll) {
        this.showAcceptAll = showAcceptAll;
    }

    public BasicInfo withShowAcceptAll(boolean showAcceptAll) {
        this.showAcceptAll = showAcceptAll;
        return this;
    }

    public String getCustomAcceptAllText() {
        return customAcceptAllText;
    }

    public void setCustomAcceptAllText(String customAcceptAllText) {
        this.customAcceptAllText = customAcceptAllText;
    }

    public BasicInfo withCustomAcceptAllText(String customAcceptAllText) {
        this.customAcceptAllText = customAcceptAllText;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicInfo info = (BasicInfo) o;
        return showJurisdiction == info.showJurisdiction &&
                showCollectionMethod == info.showCollectionMethod &&
                showDataController == info.showDataController &&
                showScope == info.showScope &&
                showShortNoticeLink == info.showShortNoticeLink &&
                showAcceptAll == info.showAcceptAll &&
                Objects.equals(logoPath, info.logoPath) &&
                Objects.equals(logoAltText, info.logoAltText) &&
                Objects.equals(title, info.title) &&
                Objects.equals(header, info.header) &&
                Objects.equals(footer, info.footer) &&
                Objects.equals(jurisdiction, info.jurisdiction) &&
                Objects.equals(collectionMethod, info.collectionMethod) &&
                Objects.equals(dataController, info.dataController) &&
                Objects.equals(scope, info.scope) &&
                Objects.equals(shortNoticeLink, info.shortNoticeLink) &&
                Objects.equals(privacyPolicyUrl, info.privacyPolicyUrl) &&
                Objects.equals(customPrivacyPolicyText, info.customPrivacyPolicyText) &&
                Objects.equals(customAcceptAllText, info.customAcceptAllText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logoPath, logoAltText, title, header, footer, jurisdiction, showJurisdiction, collectionMethod, showCollectionMethod, dataController, showDataController, scope, showScope, shortNoticeLink, showShortNoticeLink, privacyPolicyUrl, customPrivacyPolicyText, showAcceptAll, customAcceptAllText);
    }

    @Override
    public String toString() {
        return "BasicInfo{" +
                "logoPath='" + logoPath + '\'' +
                ", logoAltText='" + logoAltText + '\'' +
                ", title='" + title + '\'' +
                ", header='" + header + '\'' +
                ", footer='" + footer + '\'' +
                ", jurisdiction='" + jurisdiction + '\'' +
                ", showJurisdiction=" + showJurisdiction +
                ", collectionMethod='" + collectionMethod + '\'' +
                ", showCollectionMethod=" + showCollectionMethod +
                ", dataController=" + dataController +
                ", showDataController=" + showDataController +
                ", scope='" + scope + '\'' +
                ", showScope=" + showScope +
                ", shortNoticeLink='" + shortNoticeLink + '\'' +
                ", showShortNoticeLink=" + showShortNoticeLink +
                ", privacyPolicyUrl='" + privacyPolicyUrl + '\'' +
                ", customPrivacyPolicyText='" + customPrivacyPolicyText + '\'' +
                ", showAcceptAll=" + showAcceptAll +
                ", customAcceptAllText='" + customAcceptAllText + '\'' +
                '}';
    }
}