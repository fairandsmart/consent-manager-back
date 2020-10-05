package com.fairandsmart.consent.api.filter;

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

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {

    }
}
