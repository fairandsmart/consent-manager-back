package com.fairandsmart.consent.security.entity;

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
    @JsonIgnore
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
    @JsonIgnore
    public String owner;
    @Transient
    @JsonProperty("password")
    public String secret;
    public long creationDate;
    public long lastAccessDate;

    public static Key create(String owner, String name, String roles) {
        Key key = new Key();
        key.owner = owner;
        key.persist();
        key.username = key.id.replaceAll("-", "");
        key.name = name;
        key.secret = generate(32);
        key.password = BcryptUtil.bcryptHash(key.secret);
        key.roles = roles;
        key.creationDate = System.currentTimeMillis();
        key.lastAccessDate = -1;
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
