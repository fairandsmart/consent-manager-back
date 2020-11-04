package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelData;

public class PreviewDto {

    private String language;
    private ConsentForm.Orientation orientation;
    private ModelData data;

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

    @Override
    public String toString() {
        return "PreviewDto{" +
                "language='" + language + '\'' +
                ", orientation=" + orientation +
                ", data=" + data +
                '}';
    }

}

