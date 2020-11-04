package com.fairandsmart.consent.timestamp;

/*-
 * #%L
 * Right Consent / A Consent Manager Plateform
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

import com.fairandsmart.consent.common.config.TsaConfig;
import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xades4j.XAdES4jException;
import xades4j.production.Enveloped;
import xades4j.production.XadesSigner;
import xades4j.production.XadesSigningProfile;
import xades4j.production.XadesTSigningProfile;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.providers.impl.TSAHttpData;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class TimestampServiceBean implements TimestampService {

    private static final Logger LOGGER = Logger.getLogger(TimestampServiceBean.class.getName());

    @ConfigProperty(name = "consent.keystore.path", defaultValue = "")
    String keystorePathConfig;

    @Inject
    TsaConfig config;

    private XadesSigner signer;

    @PostConstruct
    public void init() throws TimestampServiceException {
        LOGGER.log(Level.INFO, "Initializing TimestampService");

//        expected behavior:
//        - keystorePath not defined => always copy ours (dev mode)
//        - keystorePath defined => check it exists, or give up (prod mode)

        String keystorePath;
        keystorePath = String.valueOf(keystorePathConfig);

        if ( keystorePath.startsWith("~") ) {
            keystorePath = System.getProperty("user.home") + keystorePath.substring(1);
            LOGGER.log(Level.INFO, "Setting keystore relative to user home directory : {0}", keystorePath);
        }

        File keystoreFile = new File(keystorePath);
        if (! keystoreFile.exists()) {
            LOGGER.log(Level.WARNING, "{0} do not exists, copying the default one", keystorePath);
            InputStream defaultKeystore = this.getClass().getClassLoader().getResourceAsStream("keystore.jks");
            assert defaultKeystore != null;
            try {
                if (!keystoreFile.getAbsoluteFile().getParentFile().exists()) {
                    Files.createDirectory(keystoreFile.getAbsoluteFile().getParentFile().toPath());
                }
                Files.copy(defaultKeystore, Paths.get(keystorePath));
            } catch (IOException e) {
                throw new TimestampServiceException(String.format("could not copy default keystore to %s : %s", keystorePath, e.toString()));
            }
        }

        try {
            KeyingDataProvider kp = new FileSystemKeyStoreKeyingDataProvider("JKS", keystorePath,
                    new FirstCertificateSelector(),
                    new DirectPasswordProvider("password"),
                    new DirectPasswordProvider("password"), false);
            XadesSigningProfile p = new XadesTSigningProfile(kp).withBinding(TSAHttpData.class, (config.needAuth())?new TSAHttpData(config.url(), config.username(), config.password()):new TSAHttpData(config.url()));
            signer = p.newSigner();
        } catch (Exception e) {
            throw new TimestampServiceException("Unable to initialize TimestampService");
        }
    }

    @Override
    public void timestamp(Document doc) throws TimestampServiceException {
        LOGGER.log(Level.FINE, "Generating timestamp for document");
        try {
            Element elemToSign = doc.getDocumentElement();
            new Enveloped(signer).sign(elemToSign);
        } catch ( XAdES4jException e ) {
            throw new TimestampServiceException("Unable to timestamp document", e);
        }
    }

}
