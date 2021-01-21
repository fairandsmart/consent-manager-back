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
