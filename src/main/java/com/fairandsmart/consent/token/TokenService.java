package com.fairandsmart.consent.token;

import java.util.Date;

public interface TokenService {

    String generateToken(Tokenizable tokenizable);

    String generateToken(Tokenizable tokenizable, Date expirationDate);

    String generateToken(Tokenizable tokenizable, int calendarField, int calendarAmount);

    Tokenizable readToken(String token) throws TokenServiceException, TokenExpiredException, InvalidTokenException;

}
