package com.fairandsmart.consent.notification.entity;

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

import java.util.HashMap;

public class EventArgs extends HashMap<String, String> {

    public EventArgs() {
        super();
    }

    public EventArgs addArg(String key, String value) {
        this.put(key, value);
        return this;
    }

    public static EventArgs build(String key, String value) {
        EventArgs args = new EventArgs();
        args.put(key, value);
        return args;
    }

    public static EventArgs build() {
        return new EventArgs();
    }

}
