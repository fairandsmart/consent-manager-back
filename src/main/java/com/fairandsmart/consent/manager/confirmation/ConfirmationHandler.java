package com.fairandsmart.consent.manager.confirmation;

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Transaction;
import com.fairandsmart.consent.manager.exception.ConfirmationException;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

public interface ConfirmationHandler {

    boolean canHandle(ConsentContext.Confirmation confirmation);

    Map<String, String> prepare(Transaction tx);

    void validate(Transaction tx, ConsentContext ctx, MultivaluedMap<String, String> values) throws ConfirmationException;

}
