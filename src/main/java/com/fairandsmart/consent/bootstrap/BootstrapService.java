package com.fairandsmart.consent.bootstrap;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 *
 * Authors:
 *
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class BootstrapService {

    private static final Logger LOGGER = Logger.getLogger(BootstrapService.class.getName());
    private static final List<String> supportedLanguages = Arrays.asList("fr", "en");
    private static final String defaultLanguage = "fr";

    @Inject
    LiquibaseFactory liquibaseFactory;

    @Inject
    MainConfig config;

    @Inject
    SupportService supportService;

    @Inject
    StatisticsService statisticsService;

    protected void onStart(@Observes StartupEvent ev) throws Exception {
        LOGGER.log(Level.INFO, "Application is starting, migrating database");
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
