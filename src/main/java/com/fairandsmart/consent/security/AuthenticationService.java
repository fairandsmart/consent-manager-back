package com.fairandsmart.consent.security;

import com.fairandsmart.consent.common.exception.AccessDeniedException;

public interface AuthenticationService {

    String getConnectedIdentifier();

    boolean isConnectedIdentifierAdmin();

    void ensureConnectedIdentifierIsAdmin() throws AccessDeniedException;

    boolean isConnectedIdentifierOperator();

    void ensureConnectedIdentifierIsOperator() throws AccessDeniedException;
}
