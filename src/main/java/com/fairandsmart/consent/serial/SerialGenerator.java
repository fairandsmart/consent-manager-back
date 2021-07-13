package com.fairandsmart.consent.serial;

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

import com.fairandsmart.consent.common.exception.UnexpectedException;

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

    String next(String name) throws UnexpectedException;

    long extract(String serial);

    boolean isValid(String serial);

}
