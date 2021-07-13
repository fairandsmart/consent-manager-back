package com.fairandsmart.consent.api.dto;

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
import com.fairandsmart.consent.manager.model.FormLayout;

public class PreviewDto {

    private final boolean preview = true;
    private String language;
    private FormLayout.Orientation orientation;
    private ModelData data;
    private PreviewType previewType = PreviewType.FORM;

    public PreviewDto() {
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

    public ModelData getData() {
        return data;
    }

    public void setData(ModelData data) {
        this.data = data;
    }

    public PreviewType getPreviewType() {
        return previewType;
    }

    public void setPreviewType(PreviewType previewType) {
        this.previewType = previewType;
    }

    public boolean isPreview() {
        return preview;
    }

    @Override
    public String toString() {
        return "PreviewDto{" +
                "language='" + language + '\'' +
                ", orientation=" + orientation +
                ", data=" + data +
                ", previewType=" + previewType +
                ", preview=" + preview +
                '}';
    }

    public enum PreviewType {
        FORM,
        RECEIPT,
        EMAIL
    }

}

