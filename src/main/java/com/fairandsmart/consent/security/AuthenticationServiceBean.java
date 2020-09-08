package com.fairandsmart.consent.security;

import com.fairandsmart.consent.common.config.SecurityConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AuthenticationServiceBean implements AuthenticationService {

    @Inject
    SecurityConfig securityConfig;

    @Inject
    SecurityIdentity identity;

    @Override
    public String getConnectedIdentifier() {
        return (identity != null && identity.getPrincipal() != null && identity.getPrincipal().getName() != null && identity.getPrincipal().getName().length() > 0) ? identity.getPrincipal().getName() : securityConfig.anonymousIdentifierName();
    }

    @Override
    public boolean isConnectedIdentifierAdmin() {
        return identity.hasRole(securityConfig.adminRoleName());
    }

    @Override
    public void ensureConnectedIdentifierIsAdmin() throws AccessDeniedException {
        if (!identity.hasRole(securityConfig.adminRoleName())) {
            throw new AccessDeniedException("Connected Identifier doest not has required " + securityConfig.adminRoleName() + " role.");
        }
    }

    @Override
    public boolean isConnectedIdentifierOperator() {
        return identity.hasRole(securityConfig.adminRoleName()) || identity.hasRole(securityConfig.operatorRoleName());
    }

    @Override
    public void ensureConnectedIdentifierIsOperator() throws AccessDeniedException {
        if (!identity.hasRole(securityConfig.adminRoleName()) && !identity.hasRole(securityConfig.operatorRoleName())) {
            throw new AccessDeniedException("Connected Identifier doest not has required " + securityConfig.operatorRoleName() + " role.");
        }
    }
}