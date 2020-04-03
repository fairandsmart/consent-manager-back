package com.fairandsmart.consent.token;

import com.fairandsmart.consent.manager.ConsentContext;

import java.util.Map;

public interface TokenService {

    String generateToken(ConsentContext ctx);

    String generateToken(ConsentContext ctx, int calendarField, int calendarAmount);

    ConsentContext readToken(String token) throws TokenServiceException;

}
