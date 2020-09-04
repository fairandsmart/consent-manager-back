package com.fairandsmart.consent.security;

public interface AuthenticationService {

    String getConnectedIdentifier();

    boolean isConnectedIdentifierAdmin();
}
