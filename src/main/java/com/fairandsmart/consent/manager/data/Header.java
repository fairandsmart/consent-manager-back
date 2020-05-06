package com.fairandsmart.consent.manager.data;

public class Header extends ConsentElementData {

    public static final String TYPE = "header";

    private String logoPath;
    private String logoAltText = "Logo"; /* Version par défaut ? */
    private String title;
    private String body; /* Pour donner plus de liberté, éditeur WYSIWYG (§, gras...) ? Ou juste "ajouter §" et faire une String[] de § ? */
    private String readMoreLink;
    private String customReadMoreText; /* Version par défaut dans le bundle */

    public Header() {
        this.setType(TYPE);
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Header withLogoPath(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

    public String getLogoAltText() {
        return logoAltText;
    }

    public void setLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
    }

    public Header withLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Header withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Header withBody(String body) {
        this.body = body;
        return this;
    }

    public String getReadMoreLink() {
        return readMoreLink;
    }

    public void setReadMoreLink(String readMoreLink) {
        this.readMoreLink = readMoreLink;
    }

    public Header withReadMoreLink(String readMoreLink) {
        this.readMoreLink = readMoreLink;
        return this;
    }

    public String getCustomReadMoreText() {
        return customReadMoreText;
    }

    public void setCustomReadMoreText(String customReadMoreText) {
        this.customReadMoreText = customReadMoreText;
    }

    public Header withCustomReadMoreText(String customReadMoreText) {
        this.customReadMoreText = customReadMoreText;
        return this;
    }
}
