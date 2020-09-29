package com.fairandsmart.consent.security;

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.security.entity.Key;

import java.util.List;
import java.util.Set;

public interface AuthenticationService {

    String getConnectedIdentifier();

    Set<String> listConnectedIdentifierRoles();

    boolean isConnectedIdentifierAdmin();

    void ensureConnectedIdentifierIsAdmin() throws AccessDeniedException;

    boolean isConnectedIdentifierOperator();

    void ensureConnectedIdentifierIsOperator() throws AccessDeniedException;

    boolean isConnectedIdentifierApi();

    void ensureConnectedIdentifierIsApi() throws AccessDeniedException;

    List<Key> listKeys() throws AccessDeniedException;

    Key createKey(String name) throws AccessDeniedException;

    void dropKey(String id) throws AccessDeniedException;

}
