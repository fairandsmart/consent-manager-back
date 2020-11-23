package com.fairandsmart.consent.manager;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.token.Tokenizable;

import java.util.HashMap;
import java.util.Map;

public class ConsentTransaction implements Tokenizable {

    private String transaction;

    public ConsentTransaction() {
    }

    public ConsentTransaction(String transaction) {
        this.transaction = transaction;
    }

    @Override
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
