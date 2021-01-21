package com.fairandsmart.consent.support;

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
import com.fairandsmart.consent.common.config.SupportConfig;
import com.fairandsmart.consent.support.entity.Instance;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
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

    private Instance instance;
    private String supportStatus;
    private String latestVersion;

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
    public Instance getInstance() {
        return instance;
    }

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
    public void refresh() {
        if (config.isEnabled()) {
            try {
                this.latestVersion = this.remoteCheckLatestVersion();
            } catch (SupportServiceException e) {
                LOGGER.log(Level.WARNING, "Error while checking latest version available", e);
                this.supportStatus = e.getMessage();
            }
        }
    }

    @Override
    public void init() throws SupportServiceException {
        LOGGER.log(Level.INFO, "Initialising support service");
        Optional<Instance> dbInstance = Instance.findByIdOptional(mainConfig.instance());
        if (dbInstance.isEmpty()) {
            Instance instance = new Instance();
            instance.id = mainConfig.instance();
            instance.language = mainConfig.language();
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
    }

    private String remoteCheckLatestVersion() throws SupportServiceException {
        LOGGER.log(Level.INFO, "Checking remote instance");
        if ( instance == null ) {
            throw new SupportServiceException("Unable to find instance, initialisation problem");
        }
        try {
            String latestVersion = remoteSupportService.getLatestVersion(instance.key, instance.language);
            LOGGER.log(Level.INFO, "Latest version available: " + latestVersion);
            return latestVersion;
        } catch (WebApplicationException e) {
            throw new SupportServiceException("Unable to contact support API: " + e.getResponse().getStatusInfo(), e);
        }
    }

}
