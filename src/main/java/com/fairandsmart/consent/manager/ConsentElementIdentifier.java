package com.fairandsmart.consent.manager;

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

    public static boolean isValid(String serializedIdentifier) {
        String[] parts = serializedIdentifier.split(String.valueOf(separator));
        if (parts.length != 4 || !parts[0].equals(prefix) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serialize();
    }
}
