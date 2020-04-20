package com.fairandsmart.consent.manager;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ConsentContext {

    @NotNull
    private String subject;
    @NotNull
    private Orientation orientation;
    private String headerKey;
    @NotNull @NotEmpty
    private List<String> treatmentsKeys;
    private String footerKey;
    private String referer;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ConsentContext withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public ConsentContext withOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public ConsentContext withHeaderKey(String headerKey) {
        this.headerKey = headerKey;
        return this;
    }

    public List<String> getTreatmentsKeys() {
        return treatmentsKeys;
    }

    public void setTreatmentsKeys(List<String> treatmentsKeys) {
        this.treatmentsKeys = treatmentsKeys;
    }

    public ConsentContext withTreatmentsKeys(List<String> treatmentsKeys) {
        this.treatmentsKeys = treatmentsKeys;
        return this;
    }

    public String getFooterKey() {
        return footerKey;
    }

    public void setFooterKey(String footerKey) {
        this.footerKey = footerKey;
    }

    public ConsentContext withFooterKey(String footerKey) {
        this.footerKey = footerKey;
        return this;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public ConsentContext withReferer(String referer) {
        this.referer = referer;
        return this;
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

}
