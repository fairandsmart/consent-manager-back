package com.fairandsmart.consent.timestamp;

import io.quarkus.runtime.Startup;
import org.apache.bcel.generic.NEW;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xades4j.XAdES4jException;
import xades4j.production.*;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.providers.impl.TSAHttpData;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class TimestampServiceBean implements TimestampService {

    private static final Logger LOGGER = Logger.getLogger(TimestampServiceBean.class.getName());

    @ConfigProperty(name = "consent.tsa.url")
    String url;

    @ConfigProperty(name = "consent.tsa.auth")
    boolean auth;

    @ConfigProperty(name = "consent.tsa.auth.username")
    String username;

    @ConfigProperty(name = "consent.tsa.auth.password")
    String password;

    private XadesSigner signer;

    @PostConstruct
    public void init() throws TimestampServiceException {
        LOGGER.log(Level.INFO, "Initializing TimestampService");
        try {
            Path keystore = Paths.get(this.getClass().getClassLoader().getResource("keystore.jks").toURI());
            KeyingDataProvider kp = new FileSystemKeyStoreKeyingDataProvider("JKS", keystore.toString(),
                    new FirstCertificateSelector(),
                    new DirectPasswordProvider("password"),
                    new DirectPasswordProvider("password"), false);
            XadesSigningProfile p = new XadesTSigningProfile(kp).withBinding(TSAHttpData.class, (auth)?new TSAHttpData(url, username, password):new TSAHttpData(url));
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
