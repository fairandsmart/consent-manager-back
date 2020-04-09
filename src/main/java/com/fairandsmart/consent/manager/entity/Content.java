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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
