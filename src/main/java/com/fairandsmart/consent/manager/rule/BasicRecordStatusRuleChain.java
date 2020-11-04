package com.fairandsmart.consent.manager.rule;

import com.fairandsmart.consent.manager.entity.Record;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class BasicRecordStatusRuleChain extends RecordStatusRule {

    @Inject
    IrrelevantStateRule irrelevantStateRule;

    @Inject
    ExpiredRecordRule expiredRecordRule;

    @Inject
    IrrelevantBasicInfoRule irrelevantBasicInfoRule;

    @Inject
    IrrelevantBodyRule irrelevantBodyRule;

    @Inject
    ObsoleteRecordRule obsoleteRecordRule;

    @PostConstruct
    public void init() {
        this.setNext(irrelevantStateRule);
        irrelevantStateRule.setNext(expiredRecordRule);
        expiredRecordRule.setNext(irrelevantBasicInfoRule);
        irrelevantBasicInfoRule.setNext(irrelevantBodyRule);
        irrelevantBodyRule.setNext(obsoleteRecordRule);
    }

    @Override
    public void apply(List<Record> records) {
        this.applyNext(records);
    }
}
