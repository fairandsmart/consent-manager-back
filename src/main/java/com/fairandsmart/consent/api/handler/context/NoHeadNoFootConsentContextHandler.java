package com.fairandsmart.consent.api.handler.context;

import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import io.quarkus.panache.common.Sort;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NoHeadNoFootConsentContextHandler implements ConsentContextHandler {

    @Override
    public boolean canHandle(ConsentContext ctx) {
        return StringUtils.isEmpty(ctx.getHeader()) && StringUtils.isEmpty(ctx.getFooter());
    }

    @Override
    public List<Record> findRecords(ConsentContext ctx) throws EntityNotFoundException {
        List<Record> records = new ArrayList<>();
        for (String elementKey : ctx.getElements()) {
            ModelVersion elementVersion = ModelVersion.SystemHelper.findActiveVersionByKey(ctx.getOwner(), elementKey);
            Record.find(
                    "subject = ?1 and headSerial = '' and bodySerial in ?2 and footSerial = '' and (expirationTimestamp >= ?3 or expirationTimestamp = 0)",
                    Sort.by("creationTimestamp", Sort.Direction.Descending),
                    ctx.getSubject(),
                    elementVersion.getSerials(),
                    System.currentTimeMillis()
            ).stream().findFirst().ifPresent(r -> records.add((Record) r));
        }
        return records;
    }

}
