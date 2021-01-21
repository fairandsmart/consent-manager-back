package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consents Community Edition
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

import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelData;

public class PreviewDto {

    private final boolean preview = true;
    private String language;
    private ConsentForm.Orientation orientation;
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

    public ConsentForm.Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(ConsentForm.Orientation orientation) {
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

