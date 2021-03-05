package com.fairandsmart.consent.common.config;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "consent")
public interface MainConfig {

    @ConfigProperty(name = "instance.name")
    String instance();

    @ConfigProperty(name = "instance.owner")
    String owner();

    @ConfigProperty(name = "instance.lang")
    String language();

    @ConfigProperty(name = "instance.owner")
    String owner();

    @ConfigProperty(name = "instance.import-data", defaultValue="false")
    boolean importDemoData();

    @ConfigProperty(name = "public.url")
    String publicUrl();

    @ConfigProperty(name = "private.url")
    String privateUrl();

    @ConfigProperty(name = "home")
    String home();

    @ConfigProperty(name = "processor")
    String processor();

    @ConfigProperty(name = "secret")
    String secret();

    @ConfigProperty(name = "thintoken")
    boolean useThinToken();

}
