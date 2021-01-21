package com.fairandsmart.consent.api.dto;

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

import com.fairandsmart.consent.common.validation.ModelKey;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ModelEntryDto {

    private String id;
    @NotNull
    @ModelKey
    private String key;
    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    @Size(max = 2500)
    private String description;
    @NotNull
    private String type;
    private List<ModelVersionDtoLight> versions = new ArrayList<>();

    public ModelEntryDto() {
    }

    public static ModelEntryDto fromModelEntry(ModelEntry entry, List<ModelVersion> versions) throws ModelDataSerializationException {
        ModelEntryDto dto = new ModelEntryDto();
        dto.setId(entry.id);
        dto.setKey(entry.key);
        dto.setName(entry.name);
        dto.setType(entry.type);
        dto.setDescription(entry.description);
        List<ModelVersionDtoLight> lightVersions = new ArrayList<>();
        for (ModelVersion version : versions) {
            ModelVersionDtoLight modelVersionDtoLight = ModelVersionDtoLight.fromModelVersion(version);
            lightVersions.add(modelVersionDtoLight);
        }
        dto.setVersions(lightVersions);
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ModelVersionDtoLight> getVersions() {
        return versions;
    }

    public void setVersions(List<ModelVersionDtoLight> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "ModelEntryDto{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", versions=" + versions +
                '}';
    }
}
