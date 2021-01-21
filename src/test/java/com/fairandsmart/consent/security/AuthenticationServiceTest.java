package com.fairandsmart.consent.security;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
