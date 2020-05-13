package com.fairandsmart.consent.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TokenServiceBean implements TokenService {

    private static final Logger LOGGER = Logger.getLogger(TokenService.class.getName());

    @ConfigProperty(name = "consent.token.secret")
    String secret;

    @ConfigProperty(name = "consent.token.issuer")
    String issuer;

    private static JWTVerifier verifier;
    private static Algorithm algorithm;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.FINE, "Initializing Token Verifier");
        algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    @Override
    public String generateToken(Tokenizable tokenizable, int calendarField, int calendarAmount) {
        LOGGER.log(Level.INFO, "Generating token");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(calendarField, calendarAmount);
        JWTCreator.Builder builder = JWT.create().withIssuer(tokenizable.getOwner());
        builder.withExpiresAt(calendar.getTime());
        builder.withSubject(tokenizable.getSubject());
        builder.withClaim("payloadClass", tokenizable.getClass().getName());
        builder.withIssuer(issuer);
        tokenizable.getClaims().forEach((key, value) -> builder.withClaim(key, value));
        return builder.sign(algorithm);
    }

    @Override
    public String generateToken(Tokenizable tokenizable) {
        return this.generateToken(tokenizable, Calendar.HOUR, 4);
    }

    @Override
    public Tokenizable readToken(String token) throws TokenServiceException, TokenExpiredException, InvalidTokenException {
        LOGGER.log(Level.INFO, "Reading token");
        DecodedJWT decodedJWT = getDecodedToken(token);

        if (!decodedJWT.getClaims().containsKey("payloadClass")) {
            throw new InvalidTokenException("token must contains a payloadClass claim");
        }
        try {
            Class clazz = Class.forName(decodedJWT.getClaim("payloadClass").asString());
            Tokenizable tokenizable = (Tokenizable) clazz.newInstance();
            tokenizable.setSubject(decodedJWT.getSubject());
            tokenizable.setOwner(decodedJWT.getIssuer());
            Map<String, String> claims = new HashMap<>();
            for (Map.Entry<String, Claim> claim :  decodedJWT.getClaims().entrySet() ) {
                claims.put(claim.getKey(), claim.getValue().asString());
            }
            tokenizable.setClaims(claims);
            return tokenizable;
        } catch (ClassNotFoundException e) {
            throw new TokenServiceException("Unable to load class of tokenized object");
        } catch (IllegalAccessException | InstantiationException e) {
            throw new TokenServiceException("Unable to build tokenizable", e);
        }
    }

    private DecodedJWT getDecodedToken(String token) throws TokenServiceException, InvalidTokenException, TokenExpiredException {
        LOGGER.log(Level.INFO, "Decoding token: " + token);
        if (verifier != null) {
            LOGGER.log(Level.FINE, "Verifier is not null");
            try {
                DecodedJWT decodedJWT = verifier.verify(token);
                LOGGER.log(Level.FINE, "Decoded token expiration: " + decodedJWT.getExpiresAt());
                LOGGER.log(Level.FINE, "Decoded token subject: " + decodedJWT.getSubject());
                if (decodedJWT.getExpiresAt().after(new Date())) {
                    return decodedJWT;
                } else {
                    throw new TokenExpiredException("Token expires on : " + decodedJWT.getExpiresAt().toString());
                }
            } catch (JWTDecodeException ex) {
                throw new InvalidTokenException(ex);
            }
        }
        throw new TokenServiceException("token verifier is null");
    }

}
