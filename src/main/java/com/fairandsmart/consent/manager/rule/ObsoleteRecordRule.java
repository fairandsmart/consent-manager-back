package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.manager.entity.Record;

import javax.enterprise.context.RequestScoped;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class ObsoleteRecordRule extends RecordStatusRule {

    private static final Logger LOGGER = Logger.getLogger(ObsoleteRecordRule.class.getName());

    public ObsoleteRecordRule() {
        LOGGER.log(Level.FINE, "Building new ExpiredRecordInvalidationRule");
    }

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching latest record");
        Optional<Record> optional = records.stream().filter(record ->
            record.status.equals(Record.Status.UNKNOWN)
        ).sorted(Collections.reverseOrder()).findFirst();
        if ( optional.isPresent() ) {
            Record valid = optional.get();
            LOGGER.log(Level.FINE, "marking record as valid, " + valid.id);
            valid.status = Record.Status.VALID;
            valid.statusExplanation = "most recent valid record";
        }
        records.stream().filter(record ->
            record.status.equals(Record.Status.UNKNOWN)
        ).forEach(record -> {
            LOGGER.log(Level.FINE, "marking record as obsolete, " + record.id);
            record.status = Record.Status.OBSOLETE;
            record.statusExplanation = "a more recent valid record exists";
        });
        this.applyNext(records);
    }
}
