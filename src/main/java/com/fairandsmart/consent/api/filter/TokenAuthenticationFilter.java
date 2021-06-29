package com.fairandsmart.consent.api.filter;

import com.fairandsmart.consent.common.config.SecurityConfig;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.token.*;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
@PreMatching
public class TokenAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(TokenAuthenticationFilter.class.getName());

    @Inject
    SecurityConfig config;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    TokenService tokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String token = requestContext.getUriInfo().getQueryParameters().getFirst("t");
        if (token != null) {
            LOGGER.log(Level.FINE, "Found token parameter in request: " + token);
            if (config.tokenOverride() || !authenticationService.isIdentified()) {
                LOGGER.log(Level.FINE, "Trying to extract authentication information from token");
                try {
                    AccessToken accessToken = tokenService.readToken(token);
                    requestContext.setSecurityContext(new SecurityContext() {
                        @Override
                        public Principal getUserPrincipal() {
                            return new Principal() {
                                @Override
                                public String getName() {
                                    return accessToken.getSubject();
                                }
                            };
                        }

                        @Override
                        public boolean isUserInRole(String r) {
                            return false;
                        }

                        @Override
                        public boolean isSecure() {
                            return false;
                        }

                        @Override
                        public String getAuthenticationScheme() {
                            return SecurityContext.BASIC_AUTH;
                        }
                    });
                    LOGGER.log(Level.FINE, "User is now connected with token subject: " + accessToken.getSubject());
                } catch (TokenExpiredException | UnexpectedException | InvalidTokenException e) {
                    LOGGER.log(Level.INFO, "Unable to retrieve token: " + e.getMessage() + ", avoiding token authentication");
                }
            } else {
                LOGGER.log(Level.INFO, "User is already authentified as: " + authenticationService.getConnectedIdentifier());
            }
        }
    }
}
