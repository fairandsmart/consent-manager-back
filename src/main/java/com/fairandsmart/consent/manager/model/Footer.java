package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ConsentElementData;

public class Footer extends ConsentElementData {

    public static final String TYPE = "footer";

    private String body;
    private boolean showAcceptAll = false;
    private String customAcceptAllText; /* Version par défaut dans le bundle */

    public Footer() {
        this.setType(TYPE);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Footer withBody(String body) {
        this.body = body;
        return this;
    }

    public boolean isShowAcceptAll() {
        return showAcceptAll;
    }

    public void setShowAcceptAll(boolean showAcceptAll) {
        this.showAcceptAll = showAcceptAll;
    }

    public Footer withShowAcceptAll(boolean showAcceptAll) {
        this.showAcceptAll = showAcceptAll;
        return this;
    }

    public String getCustomAcceptAllText() {
        return customAcceptAllText;
    }

    public void setCustomAcceptAllText(String customAcceptAllText) {
        this.customAcceptAllText = customAcceptAllText;
    }

    public Footer withCustomAcceptAllText(String customAcceptAllText) {
        this.customAcceptAllText = customAcceptAllText;
        return this;
    }
}
