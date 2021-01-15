package com.fairandsmart.consent.common.orm;

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

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrefixPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    private static final Logger LOGGER = Logger.getLogger(PrefixPhysicalNamingStrategy.class.getName());
    private static final String PREFIX_PROPERTY_NAME = "consent.instance";
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