package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.manager.entity.Record;

import javax.enterprise.context.RequestScoped;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class IrrelevantStateRule extends RecordStatusRule {

    private static final Logger LOGGER = Logger.getLogger(IrrelevantStateRule.class.getName());

    public IrrelevantStateRule() {
        LOGGER.log(Level.FINE, "Building new StateRecordInvalidationRule");
    }

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching committed records");
        records.stream().forEach(record -> {
            if ( record.state.equals(Record.State.COMMITTED) ) {
                //Set status to unknown allowing next rules to treat record
                record.status = Record.Status.UNKNOWN;
            } else {
                LOGGER.log(Level.FINE, "marking record as irrelevant, " + record.id);
                record.status = Record.Status.IRRELEVANT;
                record.statusExplanation = "non committed records are irrelevant";
            }
        });
        this.applyNext(records);
    }
}
