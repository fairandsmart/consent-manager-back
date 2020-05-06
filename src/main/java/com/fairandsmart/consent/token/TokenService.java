package com.fairandsmart.consent.token;

public interface TokenService {

    String generateToken(Tokenizable tokenizable);

    String generateToken(Tokenizable tokenizable, int calendarField, int calendarAmount);

    Tokenizable readToken(String token) throws TokenServiceException, TokenExpiredException, InvalidTokenException;

}
