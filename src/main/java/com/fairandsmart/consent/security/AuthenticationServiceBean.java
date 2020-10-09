package com.fairandsmart.consent.security;

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.config.SecurityConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.security.entity.AccessLog;
import com.fairandsmart.consent.security.entity.Key;
import io.quarkus.security.identity.SecurityIdentity;
import org.hibernate.exception.ConstraintViolationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.LockModeType;
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

    @Inject
    MainConfig config;

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
    @Transactional
    public void logAccess(String username) {
        long now = System.currentTimeMillis();
        Optional<AccessLog> optional = AccessLog.findByIdOptional(identity.getPrincipal().getName());
        if ( optional.isEmpty() ) {
            AccessLog log = new AccessLog();
            log.username = identity.getPrincipal().getName();
            log.timestamp = now;
            try {
                log.persistAndFlush();
                LOGGER.log(Level.FINE, "AccessLog created: " + log);
            } catch ( ConstraintViolationException e ) {
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
        List<Key> keys = Key.list("owner = ?1", config.owner());
        keys.stream().forEach(key -> AccessLog.findByIdOptional(key.username).ifPresent(log -> key.lastAccessDate = ((AccessLog)log).timestamp));
        return keys;
    }

    @Override
    @Transactional
    public Key createKey(String name) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Creating new API key");
        ensureConnectedIdentifierIsAdmin();
        return Key.create(config.owner(), name, securityConfig.apiRoleName());
    }

    @Override
    @Transactional
    public void dropKey(String id) throws AccessDeniedException {
        LOGGER.log(Level.FINE, "Dropping API key");
        ensureConnectedIdentifierIsAdmin();
        Key key = Key.findById(id);
        if (key != null && key.owner.equals(config.owner())) {
            key.delete();
        }
    }
}