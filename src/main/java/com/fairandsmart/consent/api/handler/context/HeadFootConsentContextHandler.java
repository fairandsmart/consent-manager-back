package com.fairandsmart.consent.api.handler.context;

import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class HeadFootConsentContextHandler implements ConsentContextHandler {

    @Override
    public boolean canHandle(ConsentContext ctx) {
        return (ctx.getHeader() != null && !ctx.getHeader().isEmpty()) && (ctx.getFooter() != null && !ctx.getFooter().isEmpty());
    }

    @Override
    public List<Record> findRecords(ConsentContext ctx) throws EntityNotFoundException {
        List<Record> records = new ArrayList<>();
        ModelVersion headerVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getHeader());
        ModelVersion footerVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), ctx.getFooter());
        for (String elementKey : ctx.getElements()) {
            ModelVersion elementVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), elementKey);
            Record.find(
                    "subject = ?1 and headSerial in ?2 and bodySerial in ?3 and footSerial in ?4 and (expirationTimestamp >= ?5 or expirationTimestamp = 0)",
                    Sort.by("creationTimestamp", Sort.Direction.Descending),
                    ctx.getSubject(),
                    headerVersion.getSerials(),
                    elementVersion.getSerials(),
                    footerVersion.getSerials(),
                    System.currentTimeMillis()
            ).stream().findFirst().ifPresent(r -> records.add((Record) r));
        }
        return records;
    }

}
