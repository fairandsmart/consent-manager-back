package com.fairandsmart.consent.security.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.security.SecureRandom;

@Entity
@UserDefinition
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Key extends PanacheEntityBase {

    private static final String DICT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&=+-/0123456789";
    private static SecureRandom random = new SecureRandom();

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    public String name;
    @Username
    @JsonProperty("key")
    public String username;
    @Password
    @JsonIgnore
    public String password;
    @Roles
    @JsonIgnore
    public String roles;
    @Transient
    @JsonProperty("password")
    public String secret;
    public long creationDate;
    @Transient
    public long lastAccessDate = -1;

    public static Key create(String name, String roles) {
        Key key = new Key();
        key.persist();
        key.username = "k" + key.id.replaceAll("-", "").toLowerCase();
        key.name = name;
        key.secret = generate(24);
        key.password = BcryptUtil.bcryptHash(key.secret);
        key.roles = roles;
        key.creationDate = System.currentTimeMillis();
        key.persist();
        return key;
    }

    public static String generate(int length) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(DICT.length());
            result.append(DICT.charAt(index));
        }
        return result.toString();
    }

}
