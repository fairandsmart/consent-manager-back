package com.fairandsmart.consent.serial;

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
