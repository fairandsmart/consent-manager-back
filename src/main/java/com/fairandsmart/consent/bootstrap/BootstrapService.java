package com.fairandsmart.consent.bootstrap;

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.stats.StatisticsStore;
import com.fairandsmart.consent.support.SupportService;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.runtime.StartupEvent;
import liquibase.Contexts;
import liquibase.Liquibase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class BootstrapService {

    private static final Logger LOGGER = Logger.getLogger(BootstrapService.class.getName());

    @Inject
    LiquibaseFactory liquibaseFactory;

    @Inject
    MainConfig config;

    @Inject
    SupportService supportService;

    @Inject
    StatisticsStore statisticsStore;

    protected void onStart(@Observes StartupEvent ev) throws Exception {
        LOGGER.log(Level.INFO, "Application is starting, migrating database");
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.setChangeLogParameter("instance", config.instance());
            Contexts ctx = liquibaseFactory.createContexts();
            ctx.add(config.instance());
            if (config.importDemoData()) {
                ctx.add("import");
            }
            liquibase.update(ctx, liquibaseFactory.createLabels());
            //List<ChangeSetStatus> status = liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
            statisticsStore.initStats();
        }
        supportService.checkLatestVersion();
    }
}
