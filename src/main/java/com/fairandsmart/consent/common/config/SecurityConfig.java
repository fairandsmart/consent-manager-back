package com.fairandsmart.consent.common.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "consent.security")
public interface SecurityConfig {

    @ConfigProperty(name = "auth.unauthenticated")
    String anonymousIdentifierName();

    @ConfigProperty(name = "roles.admin")
    String adminRoleName();

    @ConfigProperty(name = "roles.operator")
    String operatorRoleName();

}
