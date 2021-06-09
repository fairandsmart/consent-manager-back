package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.token.Tokenizable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class ReceiptContext implements Tokenizable {

    @NotNull
    @Schema(description = "The transaction id hold by that token", example = Placeholders.TRANSACTION_ID)
    private String transaction;
    @Schema(description = "The token allowed scopes", example = Placeholders.SCOPES)
    private List<String> scopes;
    @NotNull
    @Schema(description = "The token validity (format: PnDTnHnMn.nS)", example = Placeholders.DURATION_2D_20H, defaultValue = "P1085D")
    private String validity = "P1085D";

    public ReceiptContext() {
        this.scopes = new ArrayList<>();
    }

    @Override
    public String getSubject() {
        return transaction;
    }

    @Override
    public Tokenizable setSubject(String subject) {
        return this.withTransaction(subject);
    }

    public String getTransaction() {
        return transaction;
    }

    public ReceiptContext setTransaction(String transaction) {
        return this.withTransaction(transaction);
    }

    public ReceiptContext withTransaction(String transaction) {
        this.transaction = transaction;
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public ReceiptContext setScopes(List<String> scopes) {
        return this.withScopes(scopes);
    }

    public ReceiptContext withScopes(List<String> scopes) {
        this.setScopes(scopes);
        return this;
    }

    @Override
    @Schema(hidden = true)
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (scopes != null && !scopes.isEmpty()) {
            claims.put("scopes", this.getScopes().stream().collect(Collectors.joining(",")));
        }
        return claims;
    }

    @Override
    public Tokenizable setClaims(Map<String, String> claims) {
        if (claims.containsKey("scopes")) {
            this.setScopes(Arrays.stream(claims.get("scopes").split(",")).collect(Collectors.toList()));
        }
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
