package com.fairandsmart.consent.manager.confirmation;

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
