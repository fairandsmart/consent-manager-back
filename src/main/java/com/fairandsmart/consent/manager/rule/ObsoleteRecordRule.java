package com.fairandsmart.consent.manager.rule;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
