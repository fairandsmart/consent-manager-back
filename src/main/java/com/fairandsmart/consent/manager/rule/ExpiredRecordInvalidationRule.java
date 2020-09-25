package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.manager.entity.Record;

import javax.enterprise.context.RequestScoped;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class ExpiredRecordInvalidationRule extends RecordStatusFilterRule {

    private static final Logger LOGGER = Logger.getLogger(ExpiredRecordInvalidationRule.class.getName());

    public ExpiredRecordInvalidationRule() {
        LOGGER.log(Level.FINE, "Building new ExpiredRecordInvalidationRule");
    }

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching records with expirationTimestamp before now");
        long now = System.currentTimeMillis();
        records.stream().filter(record ->
            record.expirationTimestamp < now
        ).forEach(record -> {
            LOGGER.log(Level.FINE, "marking record as obsolete, " + record.id);
            record.status = Record.Status.OBSOLETE;
            record.statusExplanation = "expiration date reached";
        });
        this.applyNext(records);
    }
}
