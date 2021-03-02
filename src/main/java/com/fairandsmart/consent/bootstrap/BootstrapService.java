package com.fairandsmart.consent.bootstrap;

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

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.stats.StatisticsService;
import com.fairandsmart.consent.support.SupportService;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.runtime.StartupEvent;
import liquibase.Contexts;
import liquibase.Liquibase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class BootstrapService {

    private static final Logger LOGGER = Logger.getLogger(BootstrapService.class.getName());
    private static final List<String> supportedLanguages = Arrays.asList("fr", "en");
    private static final String defaultLanguage = "en";

    @Inject
    LiquibaseFactory liquibaseFactory;

    @Inject
    MainConfig config;

    @Inject
    SupportService supportService;

    @Inject
    StatisticsService statisticsService;

    protected void onStart(@Observes StartupEvent ev) throws Exception {
        LOGGER.log(Level.INFO, "Application is starting, applying bootstrap");
        Locale.setDefault(Locale.US);
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.setChangeLogParameter("instance", config.instance());
            Contexts ctx = liquibaseFactory.createContexts();
            ctx.add(config.instance());
            if (config.importDemoData()) {
                if (supportedLanguages.contains(config.language())) {
                    LOGGER.log(Level.WARNING, "Importing demo data using language: " + config.language());
                    ctx.add("import_".concat(config.language()));
                } else {
                    LOGGER.log(Level.WARNING, "Unsupported language found in config: " + config.language() + ", using default language: " + defaultLanguage);
                    ctx.add("import_".concat(defaultLanguage));
                }
            }
            liquibase.update(ctx, liquibaseFactory.createLabels());
        }
        supportService.init();
        supportService.refresh();
        statisticsService.init();
    }

}
