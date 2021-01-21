package com.fairandsmart.consent.common.orm;

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

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrefixPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    private static final Logger LOGGER = Logger.getLogger(PrefixPhysicalNamingStrategy.class.getName());
    private static final String PREFIX_PROPERTY_NAME = "consent.instance.name";
    private static String prefix = "";
    {
        Properties config = new Properties();
        try {
            config.load(PrefixPhysicalNamingStrategy.class.getClassLoader().getResourceAsStream("application.properties"));
            if (config.containsKey(PREFIX_PROPERTY_NAME) && !config.getProperty(PREFIX_PROPERTY_NAME).isEmpty()) {
                prefix = config.getProperty(PREFIX_PROPERTY_NAME).toUpperCase().concat("_");
            }
            LOGGER.log(Level.INFO, "Hibernate physical table name prefix set to: " + prefix);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to parse PREFIX_PROPERTY_NAME in config", e);
        }
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier newIdentifier = new Identifier(prefix + name.getText(), name.isQuoted());
        return super.toPhysicalTableName(newIdentifier, context);
    }

}
