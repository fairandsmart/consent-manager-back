package com.fairandsmart.consent.manager.entity;

import java.util.Set;

/**
 * Common aspects of a consent model part :
 *   - hash (static reference)
 *   - serial (dynamic reference)
 *   - is modification needs invalidation of previous consent concerned by this part
 *   - status (draft, pending, active)
 *   - language
 *   - country
 *   -
 */
public interface ModelPart {

    String getId();

    String getSerial();

    String getVersion();

    Status getStatus();

    InvalidationStatus getInvalidationStatus();

    String getCountry();

    Set<String> listAvailableLanguages();

    String getHash();

    enum Status {
        DRAFT,
        REVIEW,
        ACTIVE,
        ARCHIVED
    }

    enum InvalidationStatus {
        OUTDATED,
        REFRESH,
        GOOD
    }


}
