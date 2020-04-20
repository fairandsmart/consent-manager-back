package com.fairandsmart.consent.manager.data;

public class Information extends ModelData {

    public static final String TYPE = "information";

    private String title;
    private String body;
    private String footer;

    public Information() {
        this.setType(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Information withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Information withBody(String body) {
        this.body = body;
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Information withFooter(String footer) {
        this.footer = footer;
        return this;
    }
}
