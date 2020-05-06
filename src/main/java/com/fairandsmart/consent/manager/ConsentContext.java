package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.token.Tokenizable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class ConsentContext implements Tokenizable {

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
        this.elements = new ArrayList<>();
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

    public String getElementsString() {
        return elements.stream().collect(Collectors.joining(","));
    }

    public void setElementsString(String elements) {
        this.setElements(Arrays.asList(elements.split(",")));
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

    public String getLocale() {
        return locale;
    }

    public ConsentContext setLocale(String locale) {
        this.locale = locale;
        return this;
    }

    @Override
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if ( header != null ) {
            claims.put("header", this.getHeader());
        }
        if ( elements != null && !elements.isEmpty() ) {
            claims.put("elements", this.getElementsString());
        }
        if ( footer != null ) {
            claims.put("footer", this.getFooter());
        }
        if ( locale != null ) {
            claims.put("locale", this.getLocale());
        }
        if ( callback != null ) {
            claims.put("callback", this.getCallback());
        }
        if ( orientation != null ) {
            claims.put("orientation", this.getOrientation().name());
        }
        return claims;
    }

    @Override
    public Tokenizable setClaims(Map<String, String> claims) {
        if ( claims.containsKey("header") ) {
            this.setHeader(claims.get("header"));
        }
        if ( claims.containsKey("elements") ) {
            this.setElementsString(claims.get("elements"));
        }
        if ( claims.containsKey("footer") ) {
            this.setFooter(claims.get("footer"));
        }
        if ( claims.containsKey("locale") ) {
            this.setLocale(claims.get("locale"));
        }
        if ( claims.containsKey("callback") ) {
            this.setCallback(claims.get("callback"));
        }
        if ( claims.containsKey("orientation") ) {
            this.setOrientation(Orientation.valueOf(claims.get("orientation")));
        }
        return this;
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

}
