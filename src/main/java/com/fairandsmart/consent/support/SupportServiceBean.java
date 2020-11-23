package com.fairandsmart.consent.support;

import com.fairandsmart.consent.common.config.SupportConfig;
import com.fairandsmart.consent.support.entity.Instance;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class SupportServiceBean implements SupportService {

    private static final Logger LOGGER = Logger.getLogger(SupportService.class.getName());
    private static final String INSTANCE_ID = "42";

    private static Instance instance;

    @Inject
    SupportConfig config;

    @Inject
    @RestClient
    RemoteSupportService remoteSupportService;

    @Override
    public String checkLatestVersion() throws SupportServiceException {
        if (config.isEnabled()) {
            try {
                String latestVersion = remoteSupportService.getAvailableVersion(instance.key);
                //TODO compare to existing version
                LOGGER.log(Level.INFO, "Latest version available: " + latestVersion);
                return latestVersion;
            } catch (WebApplicationException e) {
                throw new SupportServiceException("Unable to contact support API", e);
            }
        }
        throw new SupportServiceException("Support has been disabled in configuration, re-enable if you want to contact support API");
    }

    @Scheduled(cron="0 0 0 ? * MON")
    private void autoCheckLatestVersion() {
        try {
            this.checkLatestVersion();
        } catch (SupportServiceException e) {
            LOGGER.log(Level.INFO, "Unable to check latest version available", e);
        }
    }

    @Transactional
    protected void onStart(@Observes StartupEvent ev) {
        LOGGER.log(Level.INFO, "Loading instance");
        Optional<Instance> dbInstance = Instance.findByIdOptional(INSTANCE_ID);
        if (!dbInstance.isPresent()) {
            Instance instance = new Instance();
            instance.id = INSTANCE_ID;
            instance.key = UUID.randomUUID().toString();
            instance.persist();
            this.instance = instance;
        } else {
            this.instance = dbInstance.get();
        }
        this.autoCheckLatestVersion();
    }
}
