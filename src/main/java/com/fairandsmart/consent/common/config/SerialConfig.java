package com.fairandsmart.consent.common.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "consent.serial")
public interface SerialConfig {

    @ConfigProperty(name = "slot.capacity")
    int slotCapacity();

    @ConfigProperty(name = "slot.initial")
    int slotInitialValue();

    @ConfigProperty(name = "prefix")
    String prefix();

}
