package com.fairandsmart.consent.api.dto;

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
