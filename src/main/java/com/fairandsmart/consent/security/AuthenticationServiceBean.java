package com.fairandsmart.consent.security;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.common.config.SecurityConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.security.entity.AccessLog;
import com.fairandsmart.consent.security.entity.Key;
import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class AuthenticationServiceBean implements AuthenticationService {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    @Inject
    SecurityConfig securityConfig;

    @Inject
    SecurityIdentity identity;

    @Override
    public boolean isIdentified() {
        return !getConnectedIdentifier().equals(securityConfig.anonymousIdentifierName());
    }

    @Override
    public String getConnectedIdentifier() {
        String connectedIdentifier = (identity != null && identity.getPrincipal() != null && identity.getPrincipal().getName() != null && identity.getPrincipal().getName().length() > 0) ? identity.getPrincipal().getName() : securityConfig.anonymousIdentifierName();
        LOGGER.log(Level.FINEST, "Connected Identifier: " + connectedIdentifier);
        return connectedIdentifier;
    }

    @Override
    public Set<String> listConnectedIdentifierRoles() {
        return identity.getRoles();
    }

    @Override
    public boolean isConnectedIdentifierAdmin() {
        return identity.hasRole(securityConfig.adminRoleName());
    }

    @Override
    public void ensureConnectedIdentifierIsAdmin() throws AccessDeniedException {
        if (identity.hasRole(securityConfig.adminRoleName())) {
            return;
        }
        throw new AccessDeniedException("Connected Identifier doest not has required " + securityConfig.adminRoleName() + " role.");
    }

    @Override
    public boolean isConnectedIdentifierOperator() {
        return identity.hasRole(securityConfig.adminRoleName()) || identity.hasRole(securityConfig.operatorRoleName());
    }

    @Override
    public void ensureConnectedIdentifierIsOperator() throws AccessDeniedException {
        if (identity.hasRole(securityConfig.adminRoleName()) || identity.hasRole(securityConfig.operatorRoleName())) {
            return;
        }
        throw new AccessDeniedException("Connected Identifier doest not has required " + securityConfig.operatorRoleName() + " role.");
    }

    @Override
    public boolean isConnectedIdentifierApi() {
        return identity.hasRole(securityConfig.adminRoleName()) || identity.hasRole(securityConfig.operatorRoleName()) || identity.hasRole(securityConfig.apiRoleName());
    }

    @Override
    public void ensureConnectedIdentifierIsApi() throws AccessDeniedException {
        if (identity.hasRole(securityConfig.adminRoleName()) || identity.hasRole(securityConfig.operatorRoleName()) || identity.hasRole(securityConfig.apiRoleName())) {
            return;
        }
        throw new AccessDeniedException("Connected Identifier doest not has required " + securityConfig.apiRoleName() + " role.");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logAccess() {
        long now = System.currentTimeMillis();
        Optional<AccessLog> optional = AccessLog.findByIdOptional(identity.getPrincipal().getName());
        if ( optional.isEmpty() ) {
            AccessLog log = new AccessLog();
            log.username = identity.getPrincipal().getName();
            log.timestamp = now;
            try {
                log.persistAndFlush();
                LOGGER.log(Level.FINE, "AccessLog created: " + log);
            } catch ( PersistenceException e ) {
                LOGGER.log(Level.FINE, "AccessLog already created concurrently, nothing to do");
            }
        } else {
            AccessLog log = optional.get();
            if ( log.timestamp < (now - 60000)) {
                log.timestamp = now;
                log.persist();
                LOGGER.log(Level.FINE, "AccessLog updated: " + log);
            }
        }
    }

    @Override
    public List<Key> listKeys() throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Listing all api keys");
        ensureConnectedIdentifierIsAdmin();
        List<Key> keys = Key.listAll();
        keys.forEach(key -> AccessLog.findByIdOptional(key.username).ifPresent(log -> key.lastAccessDate = ((AccessLog)log).timestamp));
        return keys;
    }

    @Override
    @Transactional
    public Key createKey(String name) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Creating new API key");
        ensureConnectedIdentifierIsAdmin();
        return Key.create(name, securityConfig.apiRoleName());
    }

    @Override
    @Transactional
    public void dropKey(String id) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Dropping API key");
        ensureConnectedIdentifierIsAdmin();
        Key.deleteById(id);
    }
}
