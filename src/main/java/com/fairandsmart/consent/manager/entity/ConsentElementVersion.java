package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.manager.ConsentElementIdentifier;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
    public String parent = "";
    public String child = "";
    public String branches;
    public String owner;
    public String defaultLocale;
    public String availableLocales;
    public Status status;
    public Revocation revocation;
    public String compatibility;
    public long creationDate;
    public long modificationDate;
    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, ConsentElementContent> content = new HashMap<>();

    public ConsentElementData getData(String locale) throws ModelDataSerializationException {
        return content.get(locale).getDataObject();
    }

    public ConsentElementIdentifier getIdentifier() {
        return new ConsentElementIdentifier(entry.type, this.serial);
    }

    public void addAvailableLocale(String locale) {
        List<String> locales = Arrays.asList(availableLocales.split(","));
        if ( !locales.contains(locale) ) {
            locales.add(locale);
            availableLocales = locales.stream().collect(Collectors.joining(","));
        }
    }

    public boolean hasLocale(String locale) {
        return content.containsKey(locale);
    }

    public enum Revocation {
        REQUIRED,
        SUPPORTS,
        NEVER
    }

    public enum Status {
        DRAFT,
        ACTIVE,
        ARCHIVED
    }

    @Override
    public String toString() {
        return "ConsentElementVersion{" +
                "id=" + id +
                ", version=" + version +
                ", entry=" + entry +
                ", serial='" + serial + '\'' +
                ", parent='" + parent + '\'' +
                ", child='" + child + '\'' +
                ", branches='" + branches + '\'' +
                ", owner='" + owner + '\'' +
                ", defaultLocale='" + defaultLocale + '\'' +
                ", availableLocales='" + availableLocales + '\'' +
                ", status=" + status +
                ", revocation=" + revocation +
                ", compatibility='" + compatibility + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", content=" + content +
                '}';
    }

    public static class HistoryHelper {

        public static ConsentElementVersion findRootVersion(List<ConsentElementVersion> versions) throws ConsentManagerException {
            return versions.stream().filter(v -> v.parent == null).findFirst().orElseThrow(() -> new ConsentManagerException("unable to find root version"));
        }

        public static ConsentElementVersion findVersion(String serial, List<ConsentElementVersion> versions) {
            return versions.stream().filter(v -> v.serial == serial).findFirst().orElseGet(null);
        }

        public static List<ConsentElementVersion> orderVersions(List<ConsentElementVersion> versions) throws ConsentManagerException {
            if ( versions.isEmpty() ) {
                return versions;
            }
            List<ConsentElementVersion> ordered = new ArrayList<>();
            ordered.add(ConsentElementVersion.HistoryHelper.findRootVersion(versions));
            String next = ordered.get(0).child;
            while ( next != null ) {
                ConsentElementVersion version = findVersion(next, versions);
                ordered.add(version);
                next = version.serial;
            }
            if ( ordered.size() != versions.size() ) {
                throw new ConsentManagerException("error while ordering versions");
            }
            return ordered;
        }

    }
}
