package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelVersion;

public class PreviewDtoFull {

    private String locale;
    private ConsentForm.Orientation orientation;
    private ModelVersion data;

    public PreviewDtoFull() {
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public PreviewDtoFull withLocale(String locale) {
        this.locale = locale;
        return this;
    }

    public ConsentForm.Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(ConsentForm.Orientation orientation) {
        this.orientation = orientation;
    }

    public PreviewDtoFull withOrientation(ConsentForm.Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public ModelVersion getData() {
        return data;
    }

    public void setData(ModelVersion data) {
        this.data = data;
    }

    public PreviewDtoFull withData(ModelVersion data) {
        this.data = data;
        return this;
    }

}
