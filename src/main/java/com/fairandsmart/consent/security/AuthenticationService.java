package com.fairandsmart.consent.security;

public interface AuthenticationService {

    boolean isAuthenticated();

    String getConnectedIdentifier();

}
