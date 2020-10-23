package com.fairandsmart.consent.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fairandsmart.consent.common.config.MainConfig;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TokenServiceBean implements TokenService {

    private static final Logger LOGGER = Logger.getLogger(TokenService.class.getName());

    @Inject
    MainConfig config;

    private static JWTVerifier verifier;
    private static Algorithm algorithm;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.FINE, "Initializing Token Verifier");
        algorithm = Algorithm.HMAC256(config.secret());
        verifier = JWT.require(algorithm).withIssuer(config.owner()).build();
    }

    @Override
    public String generateToken(Tokenizable tokenizable, Date expirationDate) {
        LOGGER.log(Level.INFO, "Generating token");
        JWTCreator.Builder builder = JWT.create().withIssuer(config.owner());
        builder.withExpiresAt(expirationDate);
        builder.withSubject(tokenizable.getSubject());
        builder.withClaim("payloadClass", tokenizable.getClass().getName());
        builder.withIssuer(config.owner());
        tokenizable.getClaims().forEach(builder::withClaim);
        return builder.sign(algorithm);
    }

    @Override
    public String generateToken(Tokenizable tokenizable, int calendarField, int calendarAmount) {
        LOGGER.log(Level.INFO, "Generating token");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(calendarField, calendarAmount);
        return generateToken(tokenizable, calendar.getTime());
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
            Class<?> clazz = Class.forName(decodedJWT.getClaim("payloadClass").asString());
            Tokenizable tokenizable = (Tokenizable) clazz.getDeclaredConstructor().newInstance();
            tokenizable.setSubject(decodedJWT.getSubject());
            Map<String, String> claims = new HashMap<>();
            for (Map.Entry<String, Claim> claim :  decodedJWT.getClaims().entrySet() ) {
                claims.put(claim.getKey(), claim.getValue().asString());
            }
            tokenizable.setClaims(claims);
            return tokenizable;
        } catch (ClassNotFoundException e) {
            throw new TokenServiceException("Unable to load class of tokenized object");
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
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
