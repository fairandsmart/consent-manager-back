package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ConsentElementIdentifier;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.data.ConsentElementData;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class ConsentElementVersion extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    @Version
    public long version;
    @ManyToOne(fetch = FetchType.EAGER)
    public ConsentElementEntry entry;
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
    public Map<String, ConsentElementContent> content = new HashMap<>();

    public ConsentElementData getData(String locale) throws ModelDataSerializationException {
        return content.get(locale).getModelData();
    }

    public ConsentElementIdentifier getIdentifier() {
        return new ConsentElementIdentifier(entry.type, this.serial);
    }

    public boolean hasLocale(String locale) {
        return content.containsKey(locale);
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

    @Override
    public String toString() {
        return "ConsentElementVersion{" +
                "id=" + id +
                ", version=" + version +
                ", entry=" + entry +
                ", serial='" + serial + '\'' +
                ", parent='" + parent + '\'' +
                ", branches='" + branches + '\'' +
                ", author='" + author + '\'' +
                ", owner='" + owner + '\'' +
                ", defaultLocale='" + defaultLocale + '\'' +
                ", availableLocales='" + availableLocales + '\'' +
                ", status=" + status +
                ", invalidation=" + invalidation +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", content=" + content +
                '}';
    }
}
