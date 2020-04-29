package com.fairandsmart.consent.manager;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class ConsentContext {

    @NotNull
    private String subject;
    @NotNull
    private Orientation orientation;
    private String header;
    @NotNull
    @NotEmpty
    private List<String> elements;
    private String footer;
    private String callback;
    private String locale;

    //TODO
    // Add field for requisite (CHECK, INFORM, RECONSENT, ...) According to existing consent, form will be displayed or not
    // Add field for receipt (DISPLAY, COOKIE, STORAGE, ...) Allows to specify how the receipt will be stored and propose to client for display or storage
    // Add field for receipt status : Allows to generate a receipt status boolean if consent conditions are meet after submission (avoid rechecking base)
/*
    private String token;
    private String requisite;
    private String receipt;
    private boolean status;

    private String optoutEmail;
    private boolean preview;
    private boolean iframe;
    private boolean attachments = false;
    private Map<String, String> userinfos;
    private Map<String, String> attributes;
    */


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

    public String getCallback() {
        return callback;
    }

    public ConsentContext setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    public String getLocale() {
        return locale;
    }

    public ConsentContext setLocale(String locale) {
        this.locale = locale;
        return this;
    }
}
