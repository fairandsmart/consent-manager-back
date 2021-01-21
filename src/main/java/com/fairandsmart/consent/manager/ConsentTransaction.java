package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
