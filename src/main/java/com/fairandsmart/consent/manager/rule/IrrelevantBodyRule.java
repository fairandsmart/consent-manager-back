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

import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;

import javax.enterprise.context.RequestScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class IrrelevantBodyRule extends RecordStatusRule {

    private static final Logger LOGGER = Logger.getLogger(IrrelevantBodyRule.class.getName());

    private Map<String, List<String>> activeSerialsCache = new HashMap<>();

    @Override
    public void apply(List<Record> records) {
        LOGGER.log(Level.FINE, "searching records with invalid body serial");
        activeSerialsCache = new HashMap<>();
        records.stream().filter(record ->
            record.status.equals(Record.Status.UNKNOWN) && record.bodyKey != null
                && !record.bodyKey.isEmpty() && !getActiveSerial(record.bodyKey).contains(record.bodySerial)
        ).forEach(record -> {
            LOGGER.log(Level.FINE, "marking record as irrelevant, " + record.id);
            record.status = Record.Status.IRRELEVANT;
            record.statusExplanation = Record.StatusExplanation.BODY_SERIAL_ARCHIVED;
        });
        this.applyNext(records);
    }

    private List<String> getActiveSerial(String key) {
        if ( !activeSerialsCache.containsKey(key) ) {
            activeSerialsCache.put(key, ModelVersion.SystemHelper.findActiveSerialsForKey(key));
        }
        return activeSerialsCache.get(key);
    }
}
