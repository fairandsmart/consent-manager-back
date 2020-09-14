package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.*;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.InvalidStatusException;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/models")
public class ModelsResource {

    private static final Logger LOGGER = Logger.getLogger(ModelsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TemplateService templateService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<ModelEntryDto> listEntries(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("order") @DefaultValue("key") String order,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction,
            @QueryParam("types") List<String> types) throws ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models");
        ModelFilter filter = new ModelFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setOrder(order);
        filter.setDirection(direction);
        filter.setTypes(types);
        CollectionPage<ModelEntry> entries = consentService.listEntries(filter);
        CollectionPage<ModelEntryDto> dto = new CollectionPage<>(entries);
        dto.setValues(entries.getValues().stream().map(e -> ModelEntryDto.fromModelEntry(e)).collect(Collectors.toList()));
        for ( ModelEntryDto entryDto : dto.getValues() ) {
            for ( ModelVersion version : consentService.getVersionHistoryForEntry(entryDto.getId()) ) {
                entryDto.getVersions().add(ModelVersionDtoLight.fromModelVersion(version));
            }
        }
        return dto;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ModelEntryDto createEntry(@Valid ModelEntryDto dto) throws EntityAlreadyExistsException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "POST /models");
        ModelEntry entry = consentService.createEntry(dto.getKey(), dto.getName(), dto.getDescription(), dto.getType());
        ModelEntryDto entryDto = ModelEntryDto.fromModelEntry(entry);
        for ( ModelVersion version : consentService.getVersionHistoryForEntry(entryDto.getId()) ) {
            entryDto.getVersions().add(ModelVersionDtoLight.fromModelVersion(version));
        }
        return entryDto;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelEntryDto getEntry(@PathParam("id") @Valid @UUID String id) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models/" + id);
        ModelEntry entry = consentService.getEntry(id);
        ModelEntryDto entryDto = ModelEntryDto.fromModelEntry(entry);
        for ( ModelVersion version : consentService.getVersionHistoryForEntry(entryDto.getId()) ) {
            entryDto.getVersions().add(ModelVersionDtoLight.fromModelVersion(version));
        }
        return entryDto;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ModelEntryDto updateEntry(@PathParam("id") @Valid @UUID String id, ModelEntryDto dto) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "PUT /models/" + id);
        ModelEntry entry = consentService.updateEntry(id, dto.getName(), dto.getDescription());
        ModelEntryDto entryDto = ModelEntryDto.fromModelEntry(entry);
        for ( ModelVersion version : consentService.getVersionHistoryForEntry(entryDto.getId()) ) {
            entryDto.getVersions().add(ModelVersionDtoLight.fromModelVersion(version));
        }
        return entryDto;
    }

    @DELETE
    @Path("/{id}")
    public void deleteEntry(@PathParam("id") @Valid @UUID String id) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "DELETE /models/" + id);
        consentService.deleteEntry(id);
    }

    @GET
    @Path("/{id}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ModelVersionDtoLight> listVersions(@PathParam("id") @Valid @UUID String id) throws ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions");
        List<ModelVersionDtoLight> dto = new ArrayList<>();
        for ( ModelVersion version : consentService.getVersionHistoryForEntry(id) ) {
            dto.add(ModelVersionDtoLight.fromModelVersion(version));
        }
        return dto;
    }

    @POST
    @Path("/{id}/versions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersionDto createVersion(@PathParam("id") @Valid @UUID String id, @Valid ModelVersionDto dto) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "POST /models/" + id + "/versions");
        ModelVersion version = consentService.createVersion(id, dto.getDefaultLocale(), dto.getData());
        return ModelVersionDto.fromModelVersion(version);
    }

    @GET
    @Path("/{id}/versions/latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestVersion(@PathParam("id") @Valid @UUID String id, @Context UriInfo info, @Context HttpHeaders headers) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions/latest");
        ModelVersion latest = consentService.findLatestVersionForEntry(id);
        UriBuilder uriBuilder = info.getBaseUriBuilder().path(ModelsResource.class).path(id).path("versions").path(latest.id);
        return Response.status(Response.Status.SEE_OTHER).location(uriBuilder.build()).build();
    }

    @GET
    @Path("/{id}/versions/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveVersion(@PathParam("id") @Valid @UUID String id, @Context UriInfo info, @Context HttpHeaders headers) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions/active");
        ModelVersion active = consentService.findActiveVersionForEntry(id);
        UriBuilder uriBuilder = info.getBaseUriBuilder().path(ModelsResource.class).path(id).path("versions").path(active.id);
        return Response.status(Response.Status.SEE_OTHER).location(uriBuilder.build()).build();
    }

    @GET
    @Path("/{id}/versions/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersionDto getVersion(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid) throws EntityNotFoundException, AccessDeniedException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions/" + vid);
        ModelVersion version = consentService.getVersion(vid);
        if ( !version.entry.id.equals(id) ) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        return ModelVersionDto.fromModelVersion(version);
    }

    @PUT
    @Path("/{id}/versions/{vid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersionDto updateVersion(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid, @Valid ModelVersionDto dto) throws EntityNotFoundException, ConsentManagerException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "PUT /models/" + id + "/versions/" + vid);
        ModelVersion version = consentService.updateVersion(vid, dto.getDefaultLocale(), dto.getData());
        if ( !version.entry.id.equals(id) ) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        return ModelVersionDto.fromModelVersion(version);
    }

    @PUT
    @Path("/{id}/versions/{vid}/status")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersionDto updateVersionStatus(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid, ModelVersion.Status status) throws EntityNotFoundException, ConsentManagerException, InvalidStatusException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "PUT /models/" + id + "/versions/" + vid + "/status");
        ModelVersion version = consentService.updateVersionStatus(vid, status);
        if ( !version.entry.id.equals(id) ) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        return ModelVersionDto.fromModelVersion(version);
    }

    @POST
    @Path("/{id}/versions/{vid}/preview")
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel previewVersion(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid, PreviewDto dto) throws TemplateServiceException, AccessDeniedException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions/" + vid + "/preview");
        ModelVersion version = consentService.getVersion(vid);
        if ( !version.entry.id.equals(id) ) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        return templateService.buildModel(version);
    }

    @DELETE
    @Path("/{id}/versions/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteVersion(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "DELETE /models/" + id + "/versions/" + vid);
        ModelVersion version = consentService.getVersion(vid);
        if ( !version.entry.id.equals(id) ) {
            throw new EntityNotFoundException("Unable to find a version with id: " + vid + " in entry with id: " + id);
        }
        consentService.deleteVersion(vid);
    }

}
