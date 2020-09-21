package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelData;

public class PreviewDto {

    private String locale;
    private ConsentForm.Orientation orientation;
    private ModelData data;

    public PreviewDto() {
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
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
                "locale='" + locale + '\'' +
                ", orientation=" + orientation +
                ", data=" + data +
                '}';
    }

}

