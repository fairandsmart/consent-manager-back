package com.fairandsmart.consent.manager;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ConsentContext {

    @NotNull
    private String subject;
    @NotNull
    private Orientation orientation;
    private String header;
    @NotNull @NotEmpty
    private List<String> treatments;
    private String footer;

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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ConsentContext withHeader(String header) {
        this.header = header;
        return this;
    }

    public List<String> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<String> treatments) {
        this.treatments = treatments;
    }

    public ConsentContext withTreatments(List<String> treatments) {
        this.treatments = treatments;
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public ConsentContext withFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

}
