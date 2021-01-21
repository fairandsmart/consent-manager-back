package com.fairandsmart.consent.security.entity;

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

import com.fairandsmart.consent.common.util.Base58;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Entity
@UserDefinition
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(name="Key", description="An API Access Key that contains information needed for Http Basic Authentication")
public class Key extends PanacheEntityBase {

    private static final String DICT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&=+-/0123456789";
    private static final SecureRandom random = new SecureRandom();

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Schema(description = "The key unique id", readOnly = true)
    public String id;
    @Schema(description = "The key name", example = "Demo KEY")
    public String name;
    @Username
    @JsonIgnore
    @Schema(hidden = true)
    public String username;
    @Password
    @JsonIgnore
    @Schema(hidden = true)
    public String password;
    @Roles
    @JsonIgnore
    @Schema(hidden = true)
    public String roles;
    @Transient
    @JsonProperty("key")
    @Schema(description = "The key to use in HttpBasic Auth (only visible at creation)", readOnly = true, name="password")
    public String secret;
    @Schema(description = "The key creation timestamp", readOnly = true)
    public long creationDate;
    @Transient
    @Schema(description = "The key last access timestamp", readOnly = true)
    public long lastAccessDate = -1;

    public static Key create(String name, String roles) {
        Key key = new Key();
        key.persist();
        key.username = Base58.encodeUUID(key.id);
        key.name = name;
        key.secret = generate(13);
        key.password = BcryptUtil.bcryptHash(key.secret);
        key.secret = Base64.getEncoder().encodeToString(key.username.concat(":").concat(key.secret).getBytes(StandardCharsets.UTF_8));
        key.roles = roles;
        key.creationDate = System.currentTimeMillis();
        key.persist();
        return key;
    }

    public static String generate(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(DICT.length());
            result.append(DICT.charAt(index));
        }
        return result.toString();
    }

}
