package com.fairandsmart.consent.token;

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
        verifier = JWT.require(algorithm).withIssuer(config.owner()).build();
    }

    @Override
    @Transactional
    public String generateToken(Tokenizable tokenizable, Date expirationDate) {
        LOGGER.log(Level.INFO, "Generating token");
        JWTCreator.Builder builder = JWT.create().withIssuer(config.owner());
        builder.withExpiresAt(expirationDate);
        builder.withSubject(tokenizable.getSubject());
        builder.withClaim("payloadClass", tokenizable.getClass().getName());
        builder.withIssuer(config.owner());
        tokenizable.getClaims().forEach(builder::withClaim);
        String token = builder.sign(algorithm);
        if (config.useThinToken()) {
            //TODO If uuid distribution is uniform, find a close uuid may be possible
            //   - maybe change to a random number generator with storage existence check and generation retry
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
        return this.generateToken(tokenizable, Calendar.HOUR, 4);
    }

    @Override
    public Tokenizable readToken(String token) throws TokenServiceException, TokenExpiredException, InvalidTokenException {
        LOGGER.log(Level.INFO, "Reading token");
        String fullToken = token;
        if (token.length() < 25) {
            LOGGER.log(Level.FINE, "Token is a thin one");
            Optional<ThinToken> entry = ThinToken.findByIdOptional(token);
            fullToken = entry.map(e -> e.value).orElseThrow(() -> new TokenExpiredException("Unable to find a thin token with key: " + token));
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

    @Scheduled(cron="0 0 1 * * ?")
    public void purgeExpiredToken() {
        LOGGER.log(Level.INFO, "Deleting expired thin tokens");
        ThinToken.delete("expires > ?1", System.currentTimeMillis()-40000000);
    }

}
