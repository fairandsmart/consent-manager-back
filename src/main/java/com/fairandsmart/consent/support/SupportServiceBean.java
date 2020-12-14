package com.fairandsmart.consent.support;

import com.fairandsmart.consent.common.config.MainConfig;
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
import javax.transaction.UserTransaction;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class SupportServiceBean implements SupportService {

    private static final Logger LOGGER = Logger.getLogger(SupportService.class.getName());

    private static Instance instance;
    private static String supportStatus;
    private static String latestVersion;

    @Inject
    MainConfig mainConfig;

    @Inject
    SupportConfig config;

    @Inject
    UserTransaction transaction;

    @Inject
    @RestClient
    RemoteSupportService remoteSupportService;

    @Override
    public String getSupportStatus() {
        if (config.isEnabled()) {
            return supportStatus;
        }
        return "Support is disabled";
    }

    @Override
    public String getLatestVersion() throws SupportServiceException {
        if (config.isEnabled()) {
            return latestVersion;
        }
        throw new SupportServiceException("Support has been disabled in configuration, re-enable if you want to contact support API");
    }

    @Override
    public String getCurrentVersion() throws SupportServiceException {
        if (config.isEnabled()) {
            try {
                InputStream resourceAsStream = this.getClass().getResourceAsStream("/META-INF/maven/com.fairandsmart/consent-manager-back/pom.properties");
                Properties prop = new Properties();
                prop.load(resourceAsStream);
                return prop.getProperty("version");
            } catch (IOException e) {
                throw new SupportServiceException("unable to get version from package", e);
            }
        }
        throw new SupportServiceException("Support has been disabled in configuration, re-enable if you want to contact support API");
    }

    @Override
    @Scheduled(cron="0 0 0 ? * MON")
    public void checkLatestVersion() {
        if (config.isEnabled()) {
            try {
                SupportServiceBean.latestVersion = this.remoteCheckLatestVersion();
            } catch (SupportServiceException e) {
                LOGGER.log(Level.WARNING, "Error while checking latest version available", e);
                SupportServiceBean.supportStatus = e.getMessage();
            }
        }
    }

    private String remoteCheckLatestVersion() throws SupportServiceException {
        LOGGER.log(Level.INFO, "Loading instance");
        Optional<Instance> dbInstance = Instance.findByIdOptional(mainConfig.instance());
        if (!dbInstance.isPresent()) {
            Instance instance = new Instance();
            instance.id = mainConfig.instance();
            instance.key = UUID.randomUUID().toString();
            try {
                transaction.begin();
                instance.persist();
                transaction.commit();
            } catch (Exception e) {
                try {
                    transaction.rollback();
                } catch (Exception e2) {
                    //
                }
                throw new SupportServiceException("Unable to create instance entity", e);
            }
            this.instance = instance;
        } else {
            this.instance = dbInstance.get();
        }
        try {
            String latestVersion = remoteSupportService.getAvailableVersion(instance.key);
            LOGGER.log(Level.INFO, "Latest version available: " + latestVersion);
            return latestVersion;
        } catch (WebApplicationException e) {
            throw new SupportServiceException("Unable to contact support API: " + e.getResponse().getStatusInfo(), e);
        }
    }

}
