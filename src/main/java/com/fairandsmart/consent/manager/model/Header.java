package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

public class Header extends ModelData {

    public static final String TYPE = "header";

    // Logo
    private String logoPath;
    private String logoAltText = "Logo";
    // Main text
    private String title;
    private String body;
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

    public Header() {
        this.setType(TYPE);
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Header withLogoPath(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

    public String getLogoAltText() {
        return logoAltText;
    }

    public void setLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
    }

    public Header withLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Header withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Header withBody(String body) {
        this.body = body;
        return this;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public Header withJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
        return this;
    }

    public boolean isShowJurisdiction() {
        return showJurisdiction;
    }

    public void setShowJurisdiction(boolean showJurisdiction) {
        this.showJurisdiction = showJurisdiction;
    }

    public Header withShowJurisdiction(boolean showJurisdiction) {
        this.showJurisdiction = showJurisdiction;
        return this;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public Header withCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
        return this;
    }

    public boolean isShowCollectionMethod() {
        return showCollectionMethod;
    }

    public void setShowCollectionMethod(boolean showCollectionMethod) {
        this.showCollectionMethod = showCollectionMethod;
    }

    public Header withShowCollectionMethod(boolean showCollectionMethod) {
        this.showCollectionMethod = showCollectionMethod;
        return this;
    }

    public Controller getDataController() {
        return dataController;
    }

    public void setDataController(Controller dataController) {
        this.dataController = dataController;
    }

    public Header withDataController(Controller dataController) {
        this.dataController = dataController;
        return this;
    }

    public boolean isShowDataController() {
        return showDataController;
    }

    public void setShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
    }

    public Header withShowDataController(boolean showDataController) {
        this.showDataController = showDataController;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Header withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public boolean isShowScope() {
        return showScope;
    }

    public void setShowScope(boolean showScope) {
        this.showScope = showScope;
    }

    public Header withShowScope(boolean showScope) {
        this.showScope = showScope;
        return this;
    }

    public String getShortNoticeLink() {
        return shortNoticeLink;
    }

    public void setShortNoticeLink(String shortNoticeLink) {
        this.shortNoticeLink = shortNoticeLink;
    }

    public Header withShortNoticeLink(String shortNoticeLink) {
        this.shortNoticeLink = shortNoticeLink;
        return this;
    }

    public boolean isShowShortNoticeLink() {
        return showShortNoticeLink;
    }

    public void setShowShortNoticeLink(boolean showShortNoticeLink) {
        this.showShortNoticeLink = showShortNoticeLink;
    }

    public Header withShowShortNoticeLink(boolean showShortNoticeLink) {
        this.showShortNoticeLink = showShortNoticeLink;
        return this;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

    public Header withPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
        return this;
    }

    public String getCustomPrivacyPolicyText() {
        return customPrivacyPolicyText;
    }

    public void setCustomPrivacyPolicyText(String customPrivacyPolicyText) {
        this.customPrivacyPolicyText = customPrivacyPolicyText;
    }

    public Header withCustomPrivacyPolicyText(String customPrivacyPolicyText) {
        this.customPrivacyPolicyText = customPrivacyPolicyText;
        return this;
    }
}
