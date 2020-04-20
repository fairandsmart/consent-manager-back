package com.fairandsmart.consent.security;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
public class AuthenticationServiceBean implements AuthenticationService {

    @ConfigProperty(name = "consent.security.auth.unauthenticated")
    String unauthentifiedUser;

    @Context
    SecurityContext security;

    @Override
    public boolean isAuthentified() {
        return ( security == null || security.getUserPrincipal() == null || security.getUserPrincipal().getName() == null || !security.getUserPrincipal().getName().equals(unauthentifiedUser));
    }

    @Override
    public String getConnectedIdentifier() {
        return (security != null && security.getUserPrincipal() != null) ? security.getUserPrincipal().getName() : unauthentifiedUser;
    }
}
