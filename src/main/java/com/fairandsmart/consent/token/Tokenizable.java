package com.fairandsmart.consent.token;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import java.util.Map;

public interface Tokenizable {

    String getSubject();

    Tokenizable setSubject(String subject);

    Map<String, String> getClaims();

    Tokenizable setClaims(Map<String, String> claims);

    default int expirationDelay() {
        return 4 * 60 * 60 * 1000;
    }

}
