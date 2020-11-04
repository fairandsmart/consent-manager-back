package com.fairandsmart.consent.manager.rule;

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
