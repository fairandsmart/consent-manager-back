package com.fairandsmart.consent.api.dto;

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

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class CounterDto {

    @Schema(description = "The counter value", readOnly = true)
    private long value;
    @Schema(description = "The timestamp to start counting", readOnly = true)
    private long fromTimestamp;
    @Schema(description = "The timestamp to stop counting", readOnly = true)
    private long toTimestamp;

    public CounterDto() {
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public CounterDto withValue(long value) {
        this.value = value;
        return this;
    }

    public long getFromTimestamp() {
        return fromTimestamp;
    }

    public void setFromTimestamp(long fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    public CounterDto withFromTimestamp(long fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
        return this;
    }

    public long getToTimestamp() {
        return toTimestamp;
    }

    public void setToTimestamp(long toTimestamp) {
        this.toTimestamp = toTimestamp;
    }

    public CounterDto withToTimestamp(long toTimestamp) {
        this.toTimestamp = toTimestamp;
        return this;
    }

    @Override
    public String toString() {
        return "CounterDto{" +
                "value=" + value +
                ", fromTimestamp=" + fromTimestamp +
                ", toTimestamp=" + toTimestamp +
                '}';
    }

}
