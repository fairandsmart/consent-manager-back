package com.fairandsmart.consent.security;

public interface AuthenticationService {

    boolean isAuthentified();

    String getConnectedIdentifier();

}
