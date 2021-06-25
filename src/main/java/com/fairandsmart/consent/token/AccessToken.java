package com.fairandsmart.consent.token;

import com.fairandsmart.consent.common.consts.Placeholders;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;

public class AccessToken {

    @NotNull
    @Schema(description = "The token subject or transaction", example = Placeholders.SHELDON)
    private String subject;
    @Schema(description = "The token allowed scopes", example = Placeholders.SCOPES)
    private List<String> scopes;
    @NotNull
    @Schema(description = "The token validity (format: PnDTnHnMn.nS)", example = Placeholders.DURATION_2D_20H, defaultValue = "P1085D")
    private String validity = "PT4H";

    public AccessToken() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AccessToken withSubject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public AccessToken withScopes(List<String> scopes) {
        this.setScopes(scopes);
        return this;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public AccessToken withValidity(String validity) {
        this.setValidity(validity);
        return this;
    }

    public long getExpirationDelay() {
        Duration duration = Duration.parse(validity);
        return duration.toMillis();
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "subject='" + subject + '\'' +
                ", scopes=" + scopes +
                ", validity='" + validity + '\'' +
                '}';
    }
}
