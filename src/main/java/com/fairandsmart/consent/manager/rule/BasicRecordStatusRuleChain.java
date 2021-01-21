package com.fairandsmart.consent.manager.rule;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
