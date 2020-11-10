package com.fairandsmart.consent.manager.rule;

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
