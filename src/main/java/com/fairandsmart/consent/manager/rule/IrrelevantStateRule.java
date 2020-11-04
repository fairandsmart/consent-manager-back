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
