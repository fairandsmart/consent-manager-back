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
    private List<String> elements;
    private String footer;
    private String referer;

    //TODO
    // Add field for requisite (CHECK, INFORM, RECONSENT, ...) According to existing consent, form will be displayed or not
    // Add field for receipt (DISPLAY, COOKIE, STORAGE, ...) Allows to specify how the receipt will be stored and propose to client for display or storage
    // Add field for receipt status : Allows to generate a receipt status boolean if consent conditions are meet after submission (avoid rechecking base)


    public ConsentContext() {

    }

    public String getSubject() {
        return subject;
    }

    public ConsentContext setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public ConsentContext setOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public ConsentContext setHeader(String header) {
        this.header = header;
        return this;
    }

    public List<String> getElements() {
        return elements;
    }

    public ConsentContext setElements(List<String> elements) {
        this.elements = elements;
        return this;
    }

    public ConsentContext addElement(String element) {
        this.elements.add(element);
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public ConsentContext setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public String getReferer() {
        return referer;
    }

    public ConsentContext setReferer(String referer) {
        this.referer = referer;
        return this;
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

}
