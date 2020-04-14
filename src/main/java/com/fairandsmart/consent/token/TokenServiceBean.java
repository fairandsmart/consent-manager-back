package com.fairandsmart.consent.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fairandsmart.consent.manager.ConsentContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TokenServiceBean  implements TokenService {

    private static final Logger LOGGER = Logger.getLogger(TokenService.class.getName());

    @ConfigProperty(name = "consent.token.secret")
    String secret;

    @ConfigProperty(name = "consent.token.issuer")
    String issuer;

    @ConfigProperty(name = "consent.token.systemid")
    String systemId;

    private static JWTVerifier verifier;
    private static Algorithm algorithm;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.FINE, "Initializing Token Verifier");
        algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    @Override
    public String generateToken(ConsentContext ctx, int calendarField, int calendarAmount) {
        LOGGER.log(Level.FINE, "Generating token for subject: " + ctx.getSubject());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(calendarField, calendarAmount);
        JWTCreator.Builder builder = JWT.create().withIssuer(issuer);
        builder.withExpiresAt(calendar.getTime());
        builder.withSubject(ctx.getSubject());
        builder.withClaim("header", ctx.getHeaderKey());
        builder.withClaim("treatments", ctx.getTreatmentsKeys());
        builder.withClaim("footer", ctx.getFooterKey());
        builder.withClaim("orientation", ctx.getOrientation().name());
        return builder.sign(algorithm);
    }

    @Override
    public String generateToken(ConsentContext ctx) {
        return this.generateToken(ctx, Calendar.HOUR, 4);
    }

    @Override
    public ConsentContext readToken(String token) throws TokenServiceException, TokenExpiredException, InvalidTokenException {
        LOGGER.log(Level.FINE, "Reading token");
        DecodedJWT decodedJWT = getDecodedToken(token);
        ConsentContext ctx = new ConsentContext();
        ctx.setSubject(decodedJWT.getSubject());
        ctx.setHeaderKey(decodedJWT.getClaim("header").asString());
        ctx.setTreatmentsKeys(decodedJWT.getClaim("treatments").asList(String.class));
        ctx.setFooterKey(decodedJWT.getClaim("footer").asString());
        ctx.setOrientation(ConsentContext.Orientation.valueOf(decodedJWT.getClaim("orientation").asString()));
        return ctx;
    }

    public DecodedJWT getDecodedToken(String token) throws TokenServiceException, InvalidTokenException, TokenExpiredException {
        LOGGER.log(Level.FINE, "Decoding token: " + token);
        if ( verifier != null ) {
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
            } catch ( JWTDecodeException ex ) {
                throw new InvalidTokenException(ex);
            }
        }
        throw new TokenServiceException("token verifier is null");
    }

}
