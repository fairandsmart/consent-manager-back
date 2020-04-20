package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.data.ModelData;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class ModelVersion extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    @Version
    public long version;
    @ManyToOne(fetch = FetchType.EAGER)
    public ModelEntry entry;
    public String serial;
    public String parent;
    public String branches;
    public String author;
    public String owner;
    public String defaultLocale;
    public String availableLocales;
    public Status status;
    public Invalidation invalidation;
    public long creationDate;
    public long modificationDate;
    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, ModelContent> content = new HashMap<>();

    public ModelData getData(String locale) throws ModelDataSerializationException {
        return content.get(locale).getModelData();
    }

    public enum Invalidation {
        INVALIDATE,
        REFRESH,
        PRESERVE
    }

    public enum Status {
        DRAFT ("draft", "review"),
        REVIEW ("review", "draft", "active"),
        ACTIVE ("active", "archived"),
        ARCHIVED ("archived", "active");

        private String name;
        private List<String> transitions;

        Status(String name, String... transitions) {
            this.name = name;
            this.transitions = Arrays.asList(transitions);
        }

        public String getName() {
            return name;
        }

        public List<String> getTransitions() {
            return transitions;
        }

        public boolean isValidTransition(String status) {
            return transitions.contains(status);
        }

        public boolean isValidTransition(Status status) {
            return transitions.contains(status.toString());
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
