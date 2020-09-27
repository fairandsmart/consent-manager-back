package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.manager.entity.Record;

import java.util.List;

public abstract class RecordStatusFilterRule {

    private RecordStatusFilterRule next;

    public abstract void apply(List<Record> records);

    public void applyNext(List<Record> records) {
        if (next != null) {
            next.apply(records);
        }
    }

    public RecordStatusFilterRule setNext(RecordStatusFilterRule next) {
        this.next = next;
        return this;
    }

}
