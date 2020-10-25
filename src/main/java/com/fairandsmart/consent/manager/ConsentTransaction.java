package com.fairandsmart.consent.manager;

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
