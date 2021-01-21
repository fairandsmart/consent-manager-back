package com.fairandsmart.consent.manager.entity;

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

import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentElementIdentifier;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
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
    @Column(length = 5000)
    public String branches;
    public String author;
    public String defaultLanguage;
    public String availableLanguages = "";
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public Type type;
    @Column(length = 5000)
    public String counterparts = "";
    public long creationDate;
    public long modificationDate;
    @ElementCollection(fetch = FetchType.LAZY)
    public Map<String, ModelContent> content = new HashMap<>();

    public ModelData getData(String language) throws ModelDataSerializationException {
        if (content.containsKey(language)) {
            return content.get(language).getDataObject();
        } else {
            return content.get(defaultLanguage).getDataObject();
        }
    }

    public ConsentElementIdentifier getIdentifier() {
        return new ConsentElementIdentifier(entry.type, entry.key, this.serial);
    }

    public void addCounterpart(String counterpart) {
        List<String> cp = getCounterParts();
        if (!cp.contains(counterpart)) {
            cp.add(counterpart);
            this.setCounterParts(cp);
        }
    }

    public void setCounterParts(List<String> counterparts) {
        if (counterparts.isEmpty()) {
            this.counterparts = "";
        } else {
            this.counterparts = String.join(",", counterparts);
        }
    }

    public List<String> getCounterParts() {
        if (counterparts == null || counterparts.isEmpty()) {
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

    public boolean hasLanguage(String language) {
        return content.containsKey(language);
    }

    @Schema(description = "the version type : use MINOR for typo fixes, MAJOR in other cases")
    public enum Type {
        MAJOR,
        MINOR
    }

    @Schema(description =
            "the version status :\n" +
            "- DRAFT : will replace an ACTIVE version when finished,\n" +
            "- ACTIVE : in use,\n" +
            "- ARCHIVED : has been replaced by another ACTIVE version"
    )
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
                ", author='" + author + '\'' +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", availableLanguages='" + availableLanguages + '\'' +
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

        public static ModelVersion findVersion(String id, List<ModelVersion> versions) {
            return versions.stream().filter(v -> v.id.equals(id)).findFirst().orElse(null);
        }

        public static List<ModelVersion> orderVersions(List<ModelVersion> versions) throws ConsentManagerException {
            if (versions.isEmpty()) {
                return versions;
            }
            List<ModelVersion> ordered = new ArrayList<>();
            ordered.add(ModelVersion.HistoryHelper.findRootVersion(versions));
            String next = ordered.get(0).child;
            while (next != null && !next.isEmpty()) {
                ModelVersion version = findVersion(next, versions);
                ordered.add(version);
                next = version.child;
            }
            if (ordered.size() != versions.size()) {
                throw new ConsentManagerException("error while ordering versions");
            }
            return ordered;
        }

    }

    public static class SystemHelper {

        public static List<ModelVersion> findActiveVersionsForKeys(List<String> keys) throws EntityNotFoundException {
            List<ModelVersion> versions = new ArrayList<>();
            for (String key: keys) {
                Optional<ModelVersion> optional = ModelVersion.find("entry.key = ?1 and status = ?2", key, ModelVersion.Status.ACTIVE).singleResultOptional();
                versions.add(optional.orElseThrow(() -> new EntityNotFoundException("unable to find an active version for entry with key: " + key)));
            }
            return versions;
        }

        public static ModelVersion findActiveVersionByKey(String key) throws EntityNotFoundException {
            Optional<ModelVersion> optional = ModelVersion.find("entry.key = ?1 and status = ?2", key, ModelVersion.Status.ACTIVE).singleResultOptional();
            return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an active version for entry with key: " + key));
        }

        public static List<String> findActiveSerialsForKey(String key) {
            Optional<ModelVersion> optional = ModelVersion.find("entry.key = ?1 and status = ?2", key, ModelVersion.Status.ACTIVE).singleResultOptional();
            return optional.isPresent() ? optional.get().getSerials() : Collections.emptyList();
        }

        public static ModelVersion findActiveVersionByEntryId(String entryId) throws EntityNotFoundException {
            Optional<ModelVersion> optional = ModelVersion.find("entry.id = ?1 and status = ?2", entryId, ModelVersion.Status.ACTIVE).singleResultOptional();
            return optional.orElseThrow(() -> new EntityNotFoundException("unable to find an active version for entry with id: " + entryId));
        }

        public static ModelVersion findModelVersionForSerial(String serial, boolean forceContentLoad) throws com.fairandsmart.consent.common.exception.EntityNotFoundException {
            Optional<ModelVersion> optional = ModelVersion.find("serial = ?1", serial).singleResultOptional();
            ModelVersion version = optional.orElseThrow(() -> new EntityNotFoundException("unable to find an entry for serial: " + serial));
            if (forceContentLoad) {
                //Force load of lazy collection, no better way found
                version.content.entrySet().stream().map(Object::toString);
            }
            return version;
        }

    }
}
