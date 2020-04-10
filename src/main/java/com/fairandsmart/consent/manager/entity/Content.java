package com.fairandsmart.consent.manager.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Content {

    public String title;
    public String body;
    public String footer;

    public Content() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Content withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Content withBody(String body) {
        this.body = body;
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Content withFooter(String footer) {
        this.footer = footer;
        return this;
    }
}
