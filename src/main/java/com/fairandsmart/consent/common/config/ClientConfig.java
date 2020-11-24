package com.fairandsmart.consent.common.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "consent.client")
public interface ClientConfig {

    @ConfigProperty(name = "user-page.enabled", defaultValue = "true")
    boolean isUserPageEnabled();

    @ConfigProperty(name = "user-page.elements", defaultValue = "")
    String userPageElements();

}
