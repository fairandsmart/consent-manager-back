package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.token.Tokenizable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

public class SubjectContext implements Tokenizable {

    @NotNull
    @Schema(description = "The subject identifier that will use the token", example = Placeholders.SHELDON)
    private String subject;
    @NotNull
    @Schema(description = "The token validity (format: PnDTnHnMn.nS)", example = Placeholders.DURATION_2D_20H, defaultValue = "P1085D")
    private String validity = "P365D";

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public Tokenizable setSubject(String subject) {
        return this.withSubject(subject);
    }

    public SubjectContext withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    @Override
    @Schema(hidden = true)
    public Map<String, String> getClaims() {
        return Collections.emptyMap();
    }

    @Override
    public Tokenizable setClaims(Map<String, String> claims) {
        return this;
    }

    @Override
    public long expirationDelay() {
        Duration duration = Duration.parse(validity);
        return duration.toMillis();
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
