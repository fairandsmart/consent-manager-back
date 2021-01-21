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

@ConfigProperties(prefix = "consent.support")
public interface SupportConfig {

    @ConfigProperty(name = "enabled", defaultValue = "true")
    boolean isEnabled();

    @ConfigProperty(name = "news")
    boolean checkNews();

    @ConfigProperty(name = "bugs")
    boolean reportBugs();

    @ConfigProperty(name = "stats")
    boolean reportStats();

}
