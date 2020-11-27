package com.fairandsmart.consent.common.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ConfigProperties(prefix = "consent.client")
public interface ClientConfig {

    @ConfigProperty(name = "user-page.enabled", defaultValue = "true")
    boolean isUserPageEnabled();

    @ConfigProperty(name = "user-page.elements")
    Optional<String> userPageElements();

    @ConfigProperty(name = "user-page.public-url")
    Optional<String> userPagePublicUrl();

}
