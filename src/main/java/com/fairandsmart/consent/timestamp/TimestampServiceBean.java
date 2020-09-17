package com.fairandsmart.consent.timestamp;

import com.fairandsmart.consent.common.config.TsaConfig;
import io.quarkus.runtime.Startup;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class TimestampServiceBean implements TimestampService {

    private static final Logger LOGGER = Logger.getLogger(TimestampServiceBean.class.getName());

    @Inject
    TsaConfig config;

    private XadesSigner signer;

    @PostConstruct
    public void init() throws TimestampServiceException {
        LOGGER.log(Level.INFO, "Initializing TimestampService");
        try {
            Path keystore1 = Paths.get(ClassLoader.getSystemClassLoader().getResource("keystore.jks").toURI());
            LOGGER.log(Level.INFO, keystore1.toString());
            LOGGER.log(Level.INFO, new File(keystore1.toString()).getAbsolutePath());
            Path keystore2 = Paths.get(this.getClass().getClassLoader().getResource("keystore.jks").toURI());
            LOGGER.log(Level.INFO, keystore2.toString());
            LOGGER.log(Level.INFO, new File(keystore2.toString()).getAbsolutePath());
            KeyingDataProvider kp = new FileSystemKeyStoreKeyingDataProvider("JKS", keystore1.toString(),
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
