package com.fairandsmart.consent.token;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.util.Base58;
import com.fairandsmart.consent.token.entity.ThinToken;
import io.quarkus.scheduler.Scheduled;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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
        verifier = JWT.require(algorithm).withIssuer(config.instance()).build();
    }

    @Override
    @Transactional
    public String generateToken(Tokenizable tokenizable, Date expirationDate) {
        LOGGER.log(Level.INFO, "Generating token");
        JWTCreator.Builder builder = JWT.create().withIssuer(config.instance());
        builder.withExpiresAt(expirationDate);
        builder.withSubject(tokenizable.getSubject());
        builder.withClaim("payloadClass", tokenizable.getClass().getName());
        builder.withIssuer(config.instance());
        tokenizable.getClaims().forEach(builder::withClaim);
        String token = builder.sign(algorithm);
        if (config.useThinToken()) {
            String key = Base58.encodeUUID(UUID.randomUUID().toString());
            LOGGER.log(Level.FINE, "Storing short token with key: " + key);
            ThinToken thinToken = new ThinToken();
            thinToken.id = key;
            thinToken.value = token;
            thinToken.expires = expirationDate.getTime();
            thinToken.persist();
            return thinToken.id;
        } else {
            return token;
        }
    }

    @Override
    @Transactional
    public String generateToken(Tokenizable tokenizable, int calendarField, int calendarAmount) {
        LOGGER.log(Level.INFO, "Generating token");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(calendarField, calendarAmount);
        return generateToken(tokenizable, calendar.getTime());
    }

    @Override
    @Transactional
    public String generateToken(Tokenizable tokenizable) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MILLISECOND, tokenizable.expirationDelay());
        return this.generateToken(tokenizable, calendar.getTime());
    }

    @Override
    public Tokenizable readToken(String token) throws TokenServiceException, TokenExpiredException, InvalidTokenException {
        LOGGER.log(Level.INFO, "Reading token");
        String fullToken = token;
        if (token.length() < 25) {
            LOGGER.log(Level.FINE, "Token is a thin one");
            Optional<ThinToken> entry = ThinToken.findByIdOptional(token);
            fullToken = entry.map(e -> e.value).orElseThrow(() -> new InvalidTokenException("Unable to find a thin token with key: " + token));
        }
        DecodedJWT decodedJWT = getDecodedToken(fullToken);

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
                return decodedJWT;
            } catch (com.auth0.jwt.exceptions.TokenExpiredException ex) {
                throw new TokenExpiredException(ex.getMessage());
            } catch (JWTDecodeException ex) {
                throw new InvalidTokenException(ex);
            }
        }
        throw new TokenServiceException("token verifier is null");
    }

    @Scheduled(cron="0 0 1 * * ?")
    @Transactional
    public void purgeExpiredToken() {
        LOGGER.log(Level.INFO, "Deleting expired thin tokens");
        ThinToken.delete("expires > ?1", System.currentTimeMillis()-40000000);
    }

}
