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
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.UnexpectedException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.Date;
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
        verifier = JWT.require(algorithm).build();
    }

    @Override
    public String generateToken(AccessToken token, Date expirationDate) {
        LOGGER.log(Level.INFO, "Generating access token: " + token);
        LOGGER.log(Level.FINE, "Token will expires at : " + expirationDate.toString() );
        JWTCreator.Builder builder = JWT.create();
        builder.withExpiresAt(expirationDate);
        builder.withSubject(token.getSubject());
        if (token.getScopes() != null && !token.getScopes().isEmpty()) {
            builder.withClaim("scopes", token.getScopes());
        }
        String stoken = builder.sign(algorithm);
        return stoken;
    }

    @Override
    @Transactional
    public String generateToken(AccessToken token) {
        return this.generateToken(token, new Date(System.currentTimeMillis() + token.getExpirationDelay()));
    }

    @Override
    public AccessToken readToken(String token) throws UnexpectedException, TokenExpiredException, InvalidTokenException {
        LOGGER.log(Level.INFO, "Reading token");
        DecodedJWT decodedJWT = getDecodedToken(token);
        AccessToken accessToken = new AccessToken();
        accessToken.setSubject(decodedJWT.getSubject());
        if (decodedJWT.getClaims().containsKey("scopes")) {
            accessToken.setScopes(decodedJWT.getClaim("scopes").asList(String.class));
        }
        return accessToken;
    }

    private DecodedJWT getDecodedToken(String token) throws UnexpectedException, InvalidTokenException, TokenExpiredException {
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
        throw new UnexpectedException("token verifier is null");
    }

    /*
    @Scheduled(cron="0 0 1 * * ?")
    @Transactional
    public void purgeExpiredToken() {
        LOGGER.log(Level.INFO, "Deleting expired thin tokens");
        ThinToken.delete("expires > ?1", System.currentTimeMillis()-40000000);
    }

     */

}
