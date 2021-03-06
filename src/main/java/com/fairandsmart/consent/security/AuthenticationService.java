package com.fairandsmart.consent.security;

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

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.security.entity.Key;

import java.util.List;

public interface AuthenticationService {

    boolean isIdentified();

    void ensureIsIdentified() throws AccessDeniedException;

    String getConnectedIdentifier();

    boolean isConnectedIdentifierAdmin();

    void ensureConnectedIdentifierIsAdmin() throws AccessDeniedException;

    boolean isConnectedIdentifierOperator();

    void ensureConnectedIdentifierIsOperator() throws AccessDeniedException;

    boolean isConnectedIdentifierApi();

    void ensureConnectedIdentifierIsApi() throws AccessDeniedException;

    void logAccess();

    List<Key> listKeys() throws AccessDeniedException;

    Key createKey(String name) throws AccessDeniedException;

    void dropKey(String id) throws AccessDeniedException;

}
