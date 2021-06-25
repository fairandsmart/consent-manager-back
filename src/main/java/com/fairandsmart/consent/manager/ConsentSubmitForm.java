package com.fairandsmart.consent.manager;

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

import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.FormLayout;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsentSubmitForm {

    //TODO Increase this field with a complete object that could create a nice display (and maybe some behaviour : repost, propagate to support, ...)
    private String error;
    @Schema(description = "The form attached token", example = "")
    private String token;
    private ModelVersion info;
    private List<ModelVersion> elements;
    private String language;
    private FormLayout.Orientation orientation;
    private Map<String, String> previousValues;
    private boolean preview = false;
    private ModelVersion theme;
    private ModelVersion notificationEmail;
    private boolean acceptAllVisible = false;
    private String acceptAllText;
    private boolean footerOnTop = false;

    public ConsentSubmitForm() {
        elements = new ArrayList<>();
        previousValues = new HashMap<>();
    }

    public ConsentSubmitForm(ConsentContext ctx) {
        this();
        this.language = ctx.getLanguage();
        this.preview = ctx.isPreview();
        this.orientation = ctx.getLayoutData().getOrientation();
        this.acceptAllVisible = ctx.getLayoutData().isAcceptAllVisible();
        this.acceptAllText = ctx.getLayoutData().getAcceptAllText();
        this.footerOnTop = ctx.getLayoutData().isFooterOnTop();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ModelVersion getInfo() {
        return info;
    }

    public void setInfo(ModelVersion info) {
        this.info = info;
    }

    public List<ModelVersion> getElements() {
        return elements;
    }

    public void setElements(List<ModelVersion> elements) {
        this.elements = elements;
    }

    public void addElement(ModelVersion element) {
        this.elements.add(element);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public FormLayout.Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(FormLayout.Orientation orientation) {
        this.orientation = orientation;
    }

    public Map<String, String> getPreviousValues() {
        return previousValues;
    }

    public void setPreviousValues(Map<String, String> previousValues) {
        this.previousValues = previousValues;
    }

    public void addPreviousValue(String key, String value) {
        this.previousValues.put(key, value);
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public ModelVersion getTheme() {
        return theme;
    }

    public void setTheme(ModelVersion theme) {
        this.theme = theme;
    }

    public ModelVersion getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(ModelVersion notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public boolean isAcceptAllVisible() {
        return acceptAllVisible;
    }

    public void setAcceptAllVisible(boolean acceptAllVisible) {
        this.acceptAllVisible = acceptAllVisible;
    }

    public String getAcceptAllText() {
        return acceptAllText;
    }

    public void setAcceptAllText(String acceptAllText) {
        this.acceptAllText = acceptAllText;
    }

    public boolean isFooterOnTop() {
        return footerOnTop;
    }

    public void setFooterOnTop(boolean footerOnTop) {
        this.footerOnTop = footerOnTop;
    }

    @Override
    public String toString() {
        return "ConsentSubmitForm{" +
                "token='" + token + '\'' +
                ", info=" + info +
                ", elements=" + elements +
                ", language='" + language + '\'' +
                ", orientation=" + orientation +
                ", previousValues=" + previousValues +
                ", preview=" + preview +
                ", theme=" + theme +
                ", notificationEmail=" + notificationEmail +
                ", acceptAllVisible=" + acceptAllVisible +
                ", acceptAllText='" + acceptAllText + '\'' +
                ", footerOnTop=" + footerOnTop +
                '}';
    }

}
