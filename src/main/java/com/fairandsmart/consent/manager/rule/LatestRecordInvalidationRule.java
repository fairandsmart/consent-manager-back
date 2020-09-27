package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.manager.entity.Record;

import javax.enterprise.context.RequestScoped;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class LatestRecordInvalidationRule extends RecordStatusFilterRule {

    private static final Logger LOGGER = Logger.getLogger(LatestRecordInvalidationRule.class.getName());

    public LatestRecordInvalidationRule() {
        LOGGER.log(Level.FINE, "Building new ExpiredRecordInvalidationRule");
    }

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching latest record");
        Optional<Record> optional = records.stream().filter(record ->
            record.status.equals(Record.Status.COMMITTED)
        ).sorted().findFirst();
        if ( optional.isPresent() ) {
            Record valid = optional.get();
            LOGGER.log(Level.FINE, "marking record as valid, " + valid.id);
            valid.status = Record.Status.VALID;
            valid.statusExplanation = "most recent record that is not obsolete";
        }
        records.stream().filter(record ->
                record.status.equals(Record.Status.COMMITTED)
        ).forEach(record -> {
            LOGGER.log(Level.FINE, "marking record as obsolete, " + record.id);
            record.status = Record.Status.OBSOLETE;
            record.statusExplanation = "a more recent record as been found and is valid";
        });
        this.applyNext(records);
    }
}
