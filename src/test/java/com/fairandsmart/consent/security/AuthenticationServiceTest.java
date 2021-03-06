package com.fairandsmart.consent.security;

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

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AuthenticationServiceTest {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationServiceTest.class.getName());

    @Inject
    AuthenticationService authenticationService;

    @Test
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testAuthenticationAsAdmin() throws AccessDeniedException {
        String connectedIdentifier = authenticationService.getConnectedIdentifier();

        LOGGER.log(Level.INFO, "Connected Identifier: " + connectedIdentifier);

        assertEquals("sheldon", connectedIdentifier);
        assertTrue(authenticationService.isConnectedIdentifierAdmin());
        assertTrue(authenticationService.isConnectedIdentifierOperator());

        authenticationService.ensureConnectedIdentifierIsAdmin();
        authenticationService.ensureConnectedIdentifierIsOperator();
    }

    @Test
    @TestSecurity(user = "leonard", roles = {"operator"})
    public void testAuthenticationAsOperator() throws AccessDeniedException {
        String connectedIdentifier = authenticationService.getConnectedIdentifier();

        LOGGER.log(Level.INFO, "Connected Identifier: " + connectedIdentifier);

        assertEquals("leonard", connectedIdentifier);
        assertFalse(authenticationService.isConnectedIdentifierAdmin());
        assertTrue(authenticationService.isConnectedIdentifierOperator());

        try {
            authenticationService.ensureConnectedIdentifierIsAdmin();
            fail("This should raise an AccessDeniedException");
        } catch (AccessDeniedException e) {
            //
        }
        authenticationService.ensureConnectedIdentifierIsOperator();
    }

    @Test
    @TestSecurity(user = "penny")
    public void testAuthenticationAsSimpleUser() {
        String connectedIdentifier = authenticationService.getConnectedIdentifier();

        LOGGER.log(Level.INFO, "Connected Identifier: " + connectedIdentifier);

        assertEquals("penny", connectedIdentifier);
        assertFalse(authenticationService.isConnectedIdentifierAdmin());
        assertFalse(authenticationService.isConnectedIdentifierOperator());

        try {
            authenticationService.ensureConnectedIdentifierIsAdmin();
            fail("This should raise an AccessDeniedException");
        } catch (AccessDeniedException e) {
            //
        }
        try {
            authenticationService.ensureConnectedIdentifierIsOperator();
            fail("This should raise an AccessDeniedException");
        } catch (AccessDeniedException e) {
            //
        }
    }

}
