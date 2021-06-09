package com.fairandsmart.consent.profile;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class AlternateProfile  implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.emptyMap();
    }

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
        return Collections.emptySet();
    }

    @Override
    public String getConfigProfile() {
        return "alternate-profile";
    }
}
