package com.fairandsmart.consent.api.handler.context;

import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Record;

import java.util.List;

public interface ConsentContextHandler {

    boolean canHandle(ConsentContext ctx);

    List<Record> findRecords(ConsentContext ctx) throws EntityNotFoundException;

}
