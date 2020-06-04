package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.manager.ConsentElementIdentifier;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelVersion extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    @Version
    public long version;
    @ManyToOne(fetch = FetchType.EAGER)
    public ModelEntry entry;
    public String serial;
    public String parent = "";
    public String child = "";
    public String branches;
    public String owner;
    public String defaultLocale;
    public String availableLocales = "";
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public Type type;
    public String counterparts = "";
    public long creationDate;
    public long modificationDate;
    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, ModelContent> content = new HashMap<>();

    public ModelData getData(String locale) throws ModelDataSerializationException {
        return content.get(locale).getDataObject();
    }

    public ConsentElementIdentifier getIdentifier() {
        return new ConsentElementIdentifier(entry.type, entry.key, this.serial);
    }

    public void addAvailableLocale(String locale) {
        List<String> locales;
        if ( availableLocales.isEmpty() ) {
            locales = new ArrayList<>();
        } else {
            locales = Arrays.asList(availableLocales.split(","));
        }
        if ( !locales.contains(locale) ) {
            locales.add(locale);
            availableLocales = String.join(",", locales);
        }
    }

    public void addCounterpart(String counterpart) {
        List<String> cp = getCounterParts();
        if ( !cp.contains(counterpart) ) {
            cp.add(counterpart);
            this.setCounterParts(cp);
        }
    }

    public void setCounterParts(List<String> counterparts) {
        if ( counterparts.isEmpty() ) {
            this.counterparts = "";
        } else {
            this.counterparts = String.join(",", counterparts);
        }
    }

    public List<String> getCounterParts() {
        if ( counterparts == null || counterparts.isEmpty() ) {
            return new ArrayList<>();
        } else {
            return Arrays.stream(counterparts.split(",")).collect(Collectors.toList());
        }
    }

    public List<String> getSerials() {
        List<String> serials = this.getCounterParts();
        serials.add(this.serial);
        return serials;
    }

    public boolean hasLocale(String locale) {
        return content.containsKey(locale);
    }

    public enum Type {
        MAJOR,
        MINOR
    }

    public enum Status {
        DRAFT,
        ACTIVE,
        ARCHIVED
    }

    @Override
    public String toString() {
        return "ModelVersion{" +
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
                ", type=" + type +
                ", counterparts='" + counterparts + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", content=" + content +
                '}';
    }

    public static class HistoryHelper {

        public static ModelVersion findRootVersion(List<ModelVersion> versions) throws ConsentManagerException {
            return versions.stream().filter(v -> v.parent.isEmpty()).findFirst().orElseThrow(() -> new ConsentManagerException("unable to find root version"));
        }

        public static ModelVersion findVersion(String serial, List<ModelVersion> versions) {
            return versions.stream().filter(v -> v.serial.equals(serial)).findFirst().orElse(null);
        }

        public static List<ModelVersion> orderVersions(List<ModelVersion> versions) throws ConsentManagerException {
            if ( versions.isEmpty() ) {
                return versions;
            }
            List<ModelVersion> ordered = new ArrayList<>();
            ordered.add(ModelVersion.HistoryHelper.findRootVersion(versions));
            String next = ordered.get(0).child;
            while ( next != null && !next.isEmpty() ) {
                ModelVersion version = findVersion(next, versions);
                ordered.add(version);
                next = version.child;
            }
            if ( ordered.size() != versions.size() ) {
                throw new ConsentManagerException("error while ordering versions");
            }
            return ordered;
        }

    }
}
