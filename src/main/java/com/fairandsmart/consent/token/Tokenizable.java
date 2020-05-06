package com.fairandsmart.consent.token;

import java.util.Map;

public interface Tokenizable {

    String getSubject();

    Tokenizable setSubject(String subject);

    Map<String, String> getClaims();

    Tokenizable setClaims(Map<String, String> claims);

}
