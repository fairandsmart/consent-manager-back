package com.fairandsmart.consent.manager.data;

public class Footer extends ConsentElementData {

    public static final String TYPE = "footer";

    private boolean showAcceptAll;
    private String customAcceptAllText; /* Version par défaut dans le bundle */

    public Footer() {
        this.setType(TYPE);
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
