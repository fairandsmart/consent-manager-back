package com.fairandsmart.consent.serial;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
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

/**
 * Ensure thread safe and fast serial number generation :
 * - locked singleton
 * - reserve a slot from db (serial window) for future in memory allocation
 * - other node or next slot is reserved from db with next slot window
 * - in case of crash, service will load next slot from db.
 *
 * Serial format must ensure controls :
 * - include a regional prefix
 * - include checksum
 * - encoded but human readable format
 */

public interface SerialGenerator {

    String next(String name) throws SerialGeneratorException;

    long extract(String serial);

    boolean isValid(String serial);

}
