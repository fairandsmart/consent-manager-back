package com.fairandsmart.consent.common.orm;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
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
