package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.token.Tokenizable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubjectContext implements Tokenizable {

    @NotNull
    @Schema(description = "The subject identifier that will use the token", example = Placeholders.SHELDON)
    private String subject;
    @NotNull
    @Schema(description = "The token allowed actions", example = Placeholders.ACTIONS)
    private List<String> actions;
    @NotNull
    @Schema(description = "The token expiration delay (in hours)")
    private int expiration;

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

    public List<String> getActions() {
        return actions;
    }

    public SubjectContext setActions(List<String> actions) {
        return this.withActions(actions);
    }

    public SubjectContext withActions(List<String> actions) {
        this.setActions(actions);
        return this;
    }

    @Override
    @Schema(hidden = true)
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (actions != null && !actions.isEmpty()) {
            claims.put("actions", this.getActions().stream().collect(Collectors.joining(",")));
        }
        return claims;
    }

    @Override
    public Tokenizable setClaims(Map<String, String> claims) {
        if (claims.containsKey("actions")) {
            this.setActions(Arrays.stream(claims.get("actions").split(",")).collect(Collectors.toList()));
        }
        return this;
    }

    @Override
    public int expirationDelay() {
        return this.expiration * 60 * 60 * 1000;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }
}
