package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.Objects;

public class Footer extends ModelData {

    public static final String TYPE = "footer";

    private String body;
    private boolean showAcceptAll = false;
    private String customAcceptAllText;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Footer footer = (Footer) o;
        return showAcceptAll == footer.showAcceptAll &&
                Objects.equals(body, footer.body) &&
                Objects.equals(customAcceptAllText, footer.customAcceptAllText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, showAcceptAll, customAcceptAllText);
    }

    @Override
    public String toString() {
        return "Footer{" +
                "body='" + body + '\'' +
                ", showAcceptAll=" + showAcceptAll +
                ", customAcceptAllText='" + customAcceptAllText + '\'' +
                '}';
    }
}
