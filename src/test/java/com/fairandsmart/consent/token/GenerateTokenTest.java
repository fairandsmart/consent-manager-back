package com.fairandsmart.consent.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fairandsmart.consent.common.util.Base58;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GenerateTokenTest {

    private static final Logger LOGGER = Logger.getLogger(GenerateTokenTest.class.getName());

    @Test
    public void generateTokenTest() throws Exception {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.add(Calendar.MILLISECOND, 3600000);
        JWTCreator.Builder builder = JWT.create();
        builder.withExpiresAt(calendar.getTime());
        builder.withSubject("mmichu@localhost");
        String token = builder.sign(Algorithm.HMAC256("tagada54"));
        LOGGER.log(Level.WARNING, "Token: " + token);
        System.out.println("Token: " + token);
        String compressed = Base58.encode(compress(token));
        LOGGER.log(Level.WARNING, "Token (compressed): " + compressed);
        System.out.println("Token (compressed): " + compressed);
        String uncompressed = decompress(Base58.decode(compressed));
        LOGGER.log(Level.WARNING, "Token (uncompressed): " + uncompressed);
        System.out.println("Token (uncompressed): " + uncompressed);
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("tagada54")).build();
        DecodedJWT decodedJWT = verifier.verify(uncompressed);
        LOGGER.log(Level.WARNING, "Decoded subject: " + decodedJWT.getSubject());
        System.out.println("Decoded subject: " + decodedJWT.getSubject());
        assertEquals("mmichu@localhost", decodedJWT.getSubject());
    }

    public static byte[] compress(String str) throws Exception {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes("UTF-8"));
        gzip.close();
        return obj.toByteArray();
    }

    public static String decompress(byte[] data) throws Exception {
        if (data == null || data.length == 0) {
            return new String();
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
            outStr += line;
        }
        return outStr;
    }
}
