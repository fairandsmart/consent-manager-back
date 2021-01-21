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

import com.fairandsmart.consent.manager.entity.Record;

import java.util.List;

public abstract class RecordStatusRule {

    private RecordStatusRule next;

    public abstract void apply(List<Record> records);

    public void applyNext(List<Record> records) {
        if (next != null) {
            next.apply(records);
        }
    }

    public RecordStatusRule setNext(RecordStatusRule next) {
        this.next = next;
        return this;
    }

}
