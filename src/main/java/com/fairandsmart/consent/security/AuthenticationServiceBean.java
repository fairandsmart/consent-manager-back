package com.fairandsmart.consent.security;

import io.quarkus.security.identity.SecurityIdentity;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AuthenticationServiceBean implements AuthenticationService {

    @ConfigProperty(name = "consent.security.auth.unauthenticated")
    String unauthentifiedUser;

    @Inject
    SecurityIdentity identity;

    @Override
    public boolean isAuthenticated() {
        return identity != null && identity.getPrincipal() != null && identity.getPrincipal().getName() != null && !identity.getPrincipal().getName().equals(unauthentifiedUser);
    }

    @Override
    public String getConnectedIdentifier() {
        return (identity != null && identity.getPrincipal() != null) ? identity.getPrincipal().getName() : unauthentifiedUser;
    }
}
