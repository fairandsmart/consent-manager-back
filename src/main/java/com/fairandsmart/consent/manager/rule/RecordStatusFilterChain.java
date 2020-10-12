package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.manager.entity.Record;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class RecordStatusFilterChain extends RecordStatusFilterRule {

    @Inject
    ExpiredRecordInvalidationRule expiredRecordInvalidationRule;

    @Inject
    InactiveBasicInfoSerialInvalidationRule inactiveHeadSerialInvalidationRule;

    @Inject
    InactiveBodySerialInvalidationRule inactiveBodySerialInvalidationRule;

    @Inject
    InactiveBodySerialInvalidationRule inactiveFootSerialInvalidationRule;

    @Inject
    LatestRecordInvalidationRule latestRecordInvalidationRule;

    @PostConstruct
    public void init() {
        this.setNext(expiredRecordInvalidationRule);
        expiredRecordInvalidationRule.setNext(inactiveHeadSerialInvalidationRule);
        inactiveHeadSerialInvalidationRule.setNext(inactiveBodySerialInvalidationRule);
        inactiveBodySerialInvalidationRule.setNext(inactiveFootSerialInvalidationRule);
        inactiveFootSerialInvalidationRule.setNext(latestRecordInvalidationRule);
    }

    @Override
    public void apply(List<Record> records) {
        this.applyNext(records);
    }
}
