package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.exception.ConsentContextSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.Duration;

@Entity
public class Transaction extends PanacheEntityBase {

    @Id
    public String id;
    @Version
    public long version;
    @Column(length = 1024)
    public String subject;
    @Column(length = 25000)
    public String context;
    public long creationTimestamp;
    public long expirationTimestamp;
    @Enumerated(EnumType.STRING)
    public State state;

    public ConsentContext getConsentContext() throws ConsentContextSerializationException {
        try {
            if (context != null && !context.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return mapper.readValue(context, ConsentContext.class);
            }
            return null;
        } catch (JsonProcessingException e) {
            throw new ConsentContextSerializationException("Unable to deserialize consent context", e);
        }
    }

    public void setConsentContext(ConsentContext ctx) throws ConsentContextSerializationException {
        try {
            if (ctx != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
                mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                this.context = mapper.writeValueAsString(ctx);
            } else {
                this.context = null;
            }
        } catch (JsonProcessingException e) {
            throw new ConsentContextSerializationException("Unable to serialize consent context", e);
        }
    }

    public void setValidity(String duration) {
        Duration d = Duration.parse(duration);
        this.expirationTimestamp = creationTimestamp + d.toMillis();
    }

    public enum State {
        NEW,
        SUBMITTED,
        COMMITTED,
        CANCELLED,
        TIMEOUT
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", subject='" + subject + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", state=" + state +
                '}';
    }
}
