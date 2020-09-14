package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.ConsentForm;

public class PreviewDto {

    private String locale;
    private ConsentForm.Orientation orientation;

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
}

