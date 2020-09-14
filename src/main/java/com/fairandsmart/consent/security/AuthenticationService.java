package com.fairandsmart.consent.security;

import com.fairandsmart.consent.common.exception.AccessDeniedException;

import java.util.Set;

public interface AuthenticationService {

    String getConnectedIdentifier();

    Set<String> listConnectedIdentifierRoles();

    boolean isConnectedIdentifierAdmin();

    void ensureConnectedIdentifierIsAdmin() throws AccessDeniedException;

    boolean isConnectedIdentifierOperator();

    void ensureConnectedIdentifierIsOperator() throws AccessDeniedException;
}
