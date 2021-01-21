package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.token.Tokenizable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

@Schema(description = "Represents a consent transaction")
public class ConsentTransaction implements Tokenizable {

    @Schema(description = "the transaction id", example = Placeholders.NIL_UUID)
    private String transaction;

    public ConsentTransaction() {
    }

    public ConsentTransaction(String transaction) {
        this.transaction = transaction;
    }

    @Override
    @Schema(hidden = true) // else appears on the generated OpenAPI object
    public String getSubject() {
        return transaction;
    }

    @Override
    public Tokenizable setSubject(String subject) {
        this.transaction = subject;
        return this;
    }

    public String getTransaction() {
        return transaction;
    }

    public ConsentTransaction setTransaction(String transaction) {
        this.transaction = transaction;
        return this;
    }

    @Override
    @Schema(hidden = true) // else appears on the generated OpenAPI object
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (transaction != null) {
            claims.put("transaction", this.getTransaction());
        }
        return claims;
    }

    @Override
    public Tokenizable setClaims(Map<String, String> claims) {
        if (claims.containsKey("transaction")) {
            this.setTransaction(claims.get("transaction"));
        }
        return this;
    }

    @Override
    public String toString() {
        return "ConsentTransaction{" +
                "transaction='" + transaction + '\'' +
                '}';
    }
}
