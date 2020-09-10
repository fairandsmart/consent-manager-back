package com.fairandsmart.consent.manager.handler;

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import io.quarkus.panache.common.Sort;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NoHeadFootConsentContextHandler implements ConsentContextHandler {

    @Inject
    MainConfig config;

    @Override
    public boolean canHandle(ConsentContext ctx) {
        return StringUtils.isEmpty(ctx.getHeader()) && !StringUtils.isEmpty(ctx.getFooter());
    }

    @Override
    public List<Record> findRecords(ConsentContext ctx) throws EntityNotFoundException {
        List<Record> records = new ArrayList<>();
        ModelVersion footerVersion = ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), ctx.getFooter());
        for (String elementKey : ctx.getElements()) {
            ModelVersion elementVersion = ModelVersion.SystemHelper.findActiveVersionByKey(config.owner(), elementKey);
            Record.find(
                    "subject = ?1 and headSerial = '' and bodySerial in ?2 and footSerial in ?3 and (expirationTimestamp >= ?4 or expirationTimestamp = 0)",
                    Sort.by("creationTimestamp", Sort.Direction.Descending),
                    ctx.getSubject(),
                    elementVersion.getSerials(),
                    footerVersion.getSerials(),
                    System.currentTimeMillis()
            ).stream().findFirst().ifPresent(r -> records.add((Record) r));
        }
        return records;
    }

}