package com.fairandsmart.consent.api.resource;

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

import com.fairandsmart.consent.api.dto.*;
import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.InvalidStatusException;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/models")
@Tag(name = "Models", description = "Operations related to content models management")
public class ModelsResource {

    private static final Logger LOGGER = Logger.getLogger(ModelsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TemplateService templateService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The list of all keys")
    })
    @Operation(summary = "List of all available models", description = "⚠️ results are paginated")
    public CollectionPage<ModelEntryDto> listEntries(
            @Parameter(description = "page number to get") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "page size") @QueryParam("size") @DefaultValue("25") int size,
            @Parameter(description = "sort by") @QueryParam("order") @DefaultValue("key") String order,
            @Parameter(description = "sort direction (asc/desc)") @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction,
            @Parameter(description = "model types to query") @QueryParam("types") List<String> types, // todo : document model types
            @Parameter(description = "model IDs to query") @QueryParam("keys") List<String> keys
    ) throws ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models");
        ModelFilter filter = new ModelFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setOrder(order);
        filter.setDirection(direction);
        filter.setTypes(types);
        filter.setKeys(keys);
        CollectionPage<ModelEntry> entries = consentService.listEntries(filter);
        CollectionPage<ModelEntryDto> dto = new CollectionPage<>(entries);
        List<ModelEntryDto> values = new ArrayList<>();
        for (ModelEntry modelEntry : entries.getValues()) {
            ModelEntryDto modelEntryDto = transformEntryToDto(modelEntry);
            values.add(modelEntryDto);
        }
        dto.setValues(values);
        return dto;
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The model has been created"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
            @APIResponse(responseCode = "409", description = "A model with the same key already exists"),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Create model")
    public ModelEntryDto createEntry(
            @Parameter(description = "the model payload") @Valid ModelEntryDto dto
    ) throws EntityAlreadyExistsException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "POST /models");
        ModelEntry entry = consentService.createEntry(dto.getKey(), dto.getName(), dto.getDescription(), dto.getType());
        return transformEntryToDto(entry);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The model has been retrieved"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
            @APIResponse(responseCode = "404", description = "The model has not been found"),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Get model")
    public ModelEntryDto getEntry(
            @Parameter(description = "the model ID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id
    ) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models/{0}", id);
        ModelEntry entry = consentService.getEntry(id);
        return transformEntryToDto(entry);
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The model has been updated"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
            @APIResponse(responseCode = "404", description = "The model has not been found"),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Update model")
    public ModelEntryDto updateEntry(
            @Parameter(description = "the model ID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            ModelEntryDto dto
    ) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "PUT /models/{0}", id);
        ModelEntry entry = consentService.updateEntry(id, dto.getName(), dto.getDescription());
        return transformEntryToDto(entry);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The model has been deleted"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
            @APIResponse(responseCode = "404", description = "The model has not been found"),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Delete model")
    public void deleteEntry(
            @Parameter(description = "the model ID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id
    ) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "DELETE /models/{0}", id);
        consentService.deleteEntry(id);
    }

    @GET
    @Path("{id}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The model versions have been retrieved"),
            @APIResponse(responseCode = "404", description = "The model has not been found"),
    })
    @Operation(summary = "Get all versions for a given model")
    public List<ModelVersionDtoLight> listVersions(
            @Parameter(description = "the model ID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id
    ) throws ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models/{0}/versions", id);
        List<ModelVersionDtoLight> dto = new ArrayList<>();
        for (ModelVersion version : consentService.getVersionHistoryForEntry(id)) {
            dto.add(ModelVersionDtoLight.fromModelVersion(version));
        }
        return dto;
    }

    @POST
    @Path("{id}/versions")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The model version has been created"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Create new version for a given model")
    public ModelVersionDto createVersion(
            @Parameter(description = "the model ID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the version payload") @Valid ModelVersionDto dto
    ) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "POST /models/{0}/versions", id);
        ModelVersion version = consentService.createVersion(id, dto.getDefaultLanguage(), dto.getData());
        return ModelVersionDto.fromModelVersion(version);
    }

    @GET
    @Path("{id}/versions/latest")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "303", description = "Go to the new location"),
            @APIResponse(responseCode = "404", description = "The model has not been found"),
    })
    @Operation(summary = "Redirect to the latest version of a model")
    public Response getModelVersions(
            @Parameter(description = "the model ID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Context UriInfo info,
            @Context HttpHeaders headers
    ) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/{0}/versions/latest", id);
        ModelVersion latest = consentService.findLatestVersionForEntry(id);
        UriBuilder uriBuilder = info.getBaseUriBuilder().path(ModelsResource.class).path(id).path("versions").path(latest.id);
        return Response.status(Response.Status.SEE_OTHER).location(uriBuilder.build()).build();
    }

    @GET
    @Path("{id}/versions/active")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "303", description = "Go to the new location"),
            @APIResponse(responseCode = "404", description = "The model has not been found"),
    })
    @Operation(summary = "Redirect to the active version of a model")
    public Response getActiveVersion(
            @Parameter(description = "the model ID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Context UriInfo info,
            @Context HttpHeaders headers
    ) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/{0}/versions/active", id);
        ModelVersion active = consentService.findActiveVersionForEntry(id);
        UriBuilder uriBuilder = info.getBaseUriBuilder().path(ModelsResource.class).path(id).path("versions").path(active.id);
        return Response.status(Response.Status.SEE_OTHER).location(uriBuilder.build()).build();
    }

    @GET
    @Path("{id}/versions/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "here is the requested version"),
            @APIResponse(responseCode = "404", description = "the model or version has not been found"),
    })
    @Operation(summary = "Get a given version of a given model")
    public ModelVersionDto getVersion(
            @Parameter(description = "the requested model UUID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the requested version UUID", example = Placeholders.NIL_UUID) @PathParam("vid") @Valid @UUID String vid
    ) throws EntityNotFoundException, AccessDeniedException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models/{0}/versions/{1}", new Object[]{id, vid});
        ModelVersion version = consentService.getVersion(vid);
        if (!version.entry.id.equals(id)) {
            throw new EntityNotFoundException("Unable to find a version with id: {0}" + vid + " in entry with id: " + id);
        }
        return ModelVersionDto.fromModelVersion(version);
    }

    @PUT
    @Transactional
    @Path("{id}/versions/{vid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "the version has been updated"),
            @APIResponse(responseCode = "404", description = "the model or version has not been found"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Update a given version of a given model", description = "only works with latest version")
    public ModelVersionDto updateVersion(
            @Parameter(description = "the requested model UUID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the requested version UUID", example = Placeholders.NIL_UUID) @PathParam("vid") @Valid @UUID String vid,
            @Valid ModelVersionDto dto
    ) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "PUT /models/{0}/versions/{1}", new Object[]{id, vid});
        ModelVersion version = consentService.updateVersion(vid, dto.getDefaultLanguage(), dto.getData());
        if (!version.entry.id.equals(id)) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        return ModelVersionDto.fromModelVersion(version);
    }

    @PUT
    @Transactional
    @Path("{id}/versions/{vid}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "the status has been updated"),
            @APIResponse(responseCode = "404", description = "the model or version has not been found"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Change a given version's status of a given model")
    public ModelVersionDto updateVersionStatus(
            @Parameter(description = "the requested model UUID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the requested version UUID", example = Placeholders.NIL_UUID) @PathParam("vid") @Valid @UUID String vid,
            @Valid ModelVersionStatusDto dto
    ) throws EntityNotFoundException, ConsentManagerException, InvalidStatusException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "PUT /models/{0}/versions/{1}/status", new Object[]{id, vid});
        ModelVersion version = consentService.updateVersionStatus(vid, dto.getStatus());
        if (!version.entry.id.equals(id)) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        return ModelVersionDto.fromModelVersion(version);
    }

    @PUT
    @Transactional
    @Path("{id}/versions/{vid}/type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "the type has been updated"),
            @APIResponse(responseCode = "404", description = "the model or version has not been found"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Change a given version's type of a given model")
    public ModelVersionDto updateVersionType(
            @Parameter(description = "the requested model UUID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the requested version UUID", example = Placeholders.NIL_UUID) @PathParam("vid") @Valid @UUID String vid,
            @Valid ModelVersionTypeDto dto
    ) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "PUT /models/{0}/versions/{1}/type", new Object[]{id, vid});
        ModelVersion version = consentService.updateVersionType(vid, dto.getType());
        return ModelVersionDto.fromModelVersion(version);
    }

    @POST
    @Path("{id}/versions/{vid}/preview")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "the preview has been generated"),
            @APIResponse(responseCode = "404", description = "the model or version has not been found"),
    })
    @Operation(summary = "Generate the preview for a given version of a given model")
    public TemplateModel previewVersion(
            @Parameter(description = "the requested model UUID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the requested version UUID", example = Placeholders.NIL_UUID) @PathParam("vid") @Valid @UUID String vid,
            PreviewDto dto
    ) throws TemplateServiceException, AccessDeniedException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "POST /models/{0}/versions/{1}/preview", new Object[]{id, vid});
        return templateService.buildModel(consentService.previewVersion(id, vid, dto));
    }

    @DELETE
    @Path("{id}/versions/{vid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "the version has been deleted"),
            @APIResponse(responseCode = "404", description = "the model or version has not been found"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Generate a given version of a given model")
    public void deleteVersion(
            @Parameter(description = "the requested model UUID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the requested version UUID", example = Placeholders.NIL_UUID) @PathParam("vid") @Valid @UUID String vid
    ) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "DELETE /models/{0}/versions/{1}", new Object[]{id, vid});
        ModelVersion version = consentService.getVersion(vid);
        if (!version.entry.id.equals(id)) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        consentService.deleteVersion(vid);
    }

    @GET
    @Path("{id}/versions/{vid}/data")
    @Produces(MediaType.WILDCARD)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "the data have been given"),
            @APIResponse(responseCode = "404", description = "the model or version has not been found"),
    })
    @Operation(summary = "Get the data for a given version of a given model")
    public Response getDataForVersion(
            @Parameter(description = "the requested model UUID", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Parameter(description = "the requested version UUID", example = Placeholders.NIL_UUID) @PathParam("vid") @Valid @UUID String vid,
            @Parameter(description = "the locale to get data for", example = "fr") @QueryParam("locale") String locale
    ) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException, IOException {
        LOGGER.log(Level.INFO, "GET /models/{0}/versions/{1}/data", new Object[]{id, vid});
        ModelVersion version = consentService.getVersion(vid);
        if (!version.entry.id.equals(id)) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        ModelData data = version.getData(locale != null ? locale : "en");
        return Response.ok(data.toMimeContent(), data.extractDataMimeType()).build();
    }

    @GET
    @Path("serials/{sid}/data")
    @Produces(MediaType.WILDCARD)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "the data have been given"),
            @APIResponse(responseCode = "404", description = "the model has not been found"),
    })
    @Operation(summary = "Get the data for a given model serial")
    public Response getDataForSerial(
            @Parameter(description = "the requested model serial", example = Placeholders.NIL_UUID) @PathParam("sid") String modelSerial,
            @Parameter(description = "the locale to get data for", example = "fr") @QueryParam("locale") String locale
    ) throws EntityNotFoundException, ModelDataSerializationException, IOException {
        LOGGER.log(Level.INFO, "GET /models/{0}/data", modelSerial);
        ModelVersion version = consentService.findVersionForSerial(modelSerial);
        if (version == null) {
            throw new EntityNotFoundException("Unable to find a version with serial: " + modelSerial);
        }
        ModelData data = version.getData(locale != null ? locale : "en");
        return Response.ok(data.toMimeContent(), data.extractDataMimeType()).build();
    }

    private ModelEntryDto transformEntryToDto(ModelEntry entry) throws ConsentManagerException, ModelDataSerializationException {
        List<ModelVersion> versions = consentService.getVersionHistoryForEntry(entry.id);
        return ModelEntryDto.fromModelEntry(entry, versions);
    }
}
