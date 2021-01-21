package com.fairandsmart.consent.manager.rule;

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
            valid.statusExplanation = Record.StatusExplanation.LATEST_VALID;
        }
        records.stream().filter(record ->
            record.status.equals(Record.Status.UNKNOWN)
        ).forEach(record -> {
            LOGGER.log(Level.FINE, "marking record as obsolete, " + record.id);
            record.status = Record.Status.OBSOLETE;
            record.statusExplanation = Record.StatusExplanation.OBSOLETE;
        });
        this.applyNext(records);
    }
}
