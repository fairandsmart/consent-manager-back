package com.fairandsmart.consent.api.filter;

import com.fairandsmart.consent.security.AuthenticationService;
import io.quarkus.security.identity.SecurityIdentity;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AccessLogFilter implements ContainerRequestFilter {

    @Inject
    SecurityIdentity identity;

    @Inject
    AuthenticationService auth;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        if ( !identity.isAnonymous() ) {
            auth.logAccess(identity.getPrincipal().getName());
        }
    }
}
