package com.fairandsmart.consent.support;

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
