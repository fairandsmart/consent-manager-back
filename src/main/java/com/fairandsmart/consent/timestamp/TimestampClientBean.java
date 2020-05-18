package com.fairandsmart.consent.timestamp;

import com.fairandsmart.consent.timestamp.dto.Timestamp;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.tsp.TimeStampResp;
import org.bouncycastle.tsp.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TimestampClientBean implements TimestampClient {

    private static final Logger LOGGER = Logger.getLogger(TimestampClientBean.class.getName());

    @ConfigProperty(name = "consent.tsa.url")
    String serverUrl;

    @Override
    public byte[] generateFile(String hashAlgo, String hash) throws TimestampClientException {
        LOGGER.log(Level.FINE, "Generating timestamp for hash: " + hash + ", hashed with algo: " + hashAlgo);
        try {
            TimeStampResponse response = this.internalGenerate(hashAlgo, hash);
            return response.getEncoded("UTF-8");
        } catch (IOException e) {
            throw new TimestampClientException("unable to generate timestamp", e);
        }
    }

    @Override
    public Timestamp generate(String hashAlgo, String hash) throws TimestampClientException {
        LOGGER.log(Level.FINE, "Generating timestamp for hash: " + hash + ", hashed with algo: " + hashAlgo);
        try {
            TimeStampResponse response = this.internalGenerate(hashAlgo, hash);
            String res = Base64.getEncoder().encodeToString(response.getEncoded("UTF-8"));
            LOGGER.log(Level.FINEST, "Timestamp base64: {0}", res);
            return new Timestamp(hashAlgo, response.getTimeStampToken().getTimeStampInfo().getGenTime().getTime(), res);
        } catch (IOException e) {
            throw new TimestampClientException("unable to generate timestamp", e);
        }
    }

    private TimeStampResponse internalGenerate(String hashAlgo, String hash) throws TimestampClientException {
        try {
            ASN1ObjectIdentifier algoId = null;
            switch (hashAlgo.toLowerCase()) {
                case "sha1" :
                case "sha-1" :
                    algoId = TSPAlgorithms.SHA1;
                    break;
                case "sha256" :
                case "sha-256" :
                    algoId = TSPAlgorithms.SHA256;
                    break;
                case "sha512" :
                case "sha-512" :
                    algoId = TSPAlgorithms.SHA512;
                    break;
            }

            byte[] bhash = hexToByte(hash);
            TimeStampRequestGenerator timeStampRequestGenerator = new TimeStampRequestGenerator();
            TimeStampRequest timeStampRequest = timeStampRequestGenerator.generate(algoId, bhash);
            byte[] request = timeStampRequest.getEncoded();

            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/timestamp-query");
            con.setRequestProperty("Content-length", String.valueOf(request.length));
            OutputStream out = con.getOutputStream();
            out.write(request);
            out.flush();

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Received HTTP error: " + con.getResponseCode() + " - " + con.getResponseMessage());
            } else {
                LOGGER.log(Level.FINE, "Response Code: ".concat(Integer.toString(con.getResponseCode())));
            }
            InputStream in = con.getInputStream();
            TimeStampResp resp = TimeStampResp.getInstance(new ASN1InputStream(in).readObject());
            TimeStampResponse response = new TimeStampResponse(resp);
            response.validate(timeStampRequest);

            if ( response.getStatus() != 0 ) {
                LOGGER.log(Level.SEVERE, "TSA Response Status Error: {0}", response.getStatusString());
                throw new TimestampClientException("TSA response status error: " + response.getStatusString());
            }

            if ( response.getTimeStampToken() == null ) {
                LOGGER.log(Level.SEVERE, "TSA Response does not contains token");
                throw new TimestampClientException("TSA response does not contains token");
            }

            LOGGER.log(Level.FINE, "Status = {0}", response.getStatusString());
            LOGGER.log(Level.FINEST, "Timestamp: {0}", response.getTimeStampToken().getTimeStampInfo().getGenTime());
            LOGGER.log(Level.FINEST, "TSA: {0}", response.getTimeStampToken().getTimeStampInfo().getTsa());
            LOGGER.log(Level.FINEST, "Serial number: {0}", response.getTimeStampToken().getTimeStampInfo().getSerialNumber());
            LOGGER.log(Level.FINEST, "Policy: {0}", response.getTimeStampToken().getTimeStampInfo().getPolicy());

            return response;
        } catch (IOException | TSPException | DecoderException e) {
            throw new TimestampClientException("unable to generate timestamp", e);
        }
    }

    private byte[] hexToByte(String hex) throws DecoderException {
        return Hex.decodeHex(hex);
    }

    private String byteToHex(byte[] value) {
        return Hex.encodeHexString(value);
    }

}
