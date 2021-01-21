package com.fairandsmart.consent.manager;

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

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class ConsentElementIdentifier {

    public static final String prefix = "element";
    public static final Character separator = '/';

    private String type;
    private String key;
    private String serial;

    public ConsentElementIdentifier(String type, String key, String serial) {
        this.type = type;
        this.key = key;
        this.serial = serial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public static Optional<ConsentElementIdentifier> deserialize(String serializedIdentifier) {
        if (StringUtils.isNotEmpty(serializedIdentifier)) {
            String[] parts = serializedIdentifier.split(String.valueOf(separator));
            if (parts.length != 4 || !parts[0].equals(prefix)) {
                return Optional.empty();
            }
            return Optional.of(new ConsentElementIdentifier(parts[1], parts[2], parts[3]));
        }
        return Optional.empty();
    }

    public String serialize() {
        return prefix + separator + type + separator + key + separator + serial;
    }

    @Override
    public String toString() {
        return serialize();
    }
}
