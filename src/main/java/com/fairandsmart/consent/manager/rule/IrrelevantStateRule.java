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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class IrrelevantStateRule extends RecordStatusRule {

    private static final Logger LOGGER = Logger.getLogger(IrrelevantStateRule.class.getName());

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching committed records");
        records.forEach(record -> {
            if ( record.state.equals(Record.State.COMMITTED) ) {
                //Set status to unknown allowing next rules to treat record
                record.status = Record.Status.UNKNOWN;
            } else {
                LOGGER.log(Level.FINE, "marking record as irrelevant, " + record.id);
                record.status = Record.Status.IRRELEVANT;
                record.statusExplanation = Record.StatusExplanation.NOT_COMMITTED;
            }
        });
        this.applyNext(records);
    }
}
