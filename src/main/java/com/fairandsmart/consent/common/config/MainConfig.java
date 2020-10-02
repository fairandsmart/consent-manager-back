package com.fairandsmart.consent.common.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "consent")
public interface MainConfig {

    @ConfigProperty(name = "instance.name", defaultValue = "demo")
    String name();

    @ConfigProperty(name = "instance.owner",  defaultValue = "demo")
    String owner();

    @ConfigProperty(name = "public.url")
    String publicUrl();

    @ConfigProperty(name = "home")
    String home();

    @ConfigProperty(name = "processor")
    String processor();

    @ConfigProperty(name = "secret")
    String secret();

}
