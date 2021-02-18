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
public class ExpiredRecordRule extends RecordStatusRule {

    private static final Logger LOGGER = Logger.getLogger(ExpiredRecordRule.class.getName());

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching records with expirationTimestamp before now");
        long now = System.currentTimeMillis();
        records.stream().filter(record ->
            record.status.equals(Record.Status.UNKNOWN) &&
            record.expirationTimestamp > 0 &&
            record.expirationTimestamp < now
        ).forEach(record -> {
            LOGGER.log(Level.FINE, "marking record as expired, " + record.id);
            record.status = Record.Status.EXPIRED;
            record.statusExplanation = Record.StatusExplanation.EXPIRED;
        });
        this.applyNext(records);
    }
}
