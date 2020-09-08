package com.fairandsmart.consent.common.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "consent.tsa")
public interface TsaConfig {

    @ConfigProperty(name = "url")
    String url();

    @ConfigProperty(name = "auth")
    boolean needAuth();

    @ConfigProperty(name = "auth.username")
    String username();

    @ConfigProperty(name = "auth.password")
    String password();

}
