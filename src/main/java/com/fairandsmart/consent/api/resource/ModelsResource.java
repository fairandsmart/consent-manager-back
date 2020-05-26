package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.*;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.InvalidStatusException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.sun.jndi.toolkit.url.Uri;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/models")
@RolesAllowed("admin")
public class ModelsResource {

    private static final Logger LOGGER = Logger.getLogger(ModelsResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<ModelEntry> listEntries(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("types") List<String> types) {
        LOGGER.log(Level.INFO, "GET /models");
        ModelFilter filter = new ModelFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setTypes(types);
        return consentService.listEntries(filter);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ModelEntry createEntry(@Valid CreateModelDto dto) throws EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "POST /models");
        return consentService.createEntry(dto.getKey(), dto.getName(), dto.getDescription(), dto.getType());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelEntry getEntry(@PathParam("id") @Valid @UUID String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /models/" + id);
        return consentService.getEntry(id);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelEntry updateEntry(@PathParam("id") @Valid @UUID String id, UpdateModelDto dto) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "PUT /models/" + id);
        return consentService.updateEntry(id, dto.getName(), dto.getDescription());
    }

    @GET
    @Path("/{id}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ModelVersion> listEntryVersions(@PathParam("id") @Valid @UUID String id) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions");
        return consentService.getVersionHistoryForEntry(id);
    }

    @POST
    @Path("/{id}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersion createVersion(@PathParam("id") @Valid @UUID String id, ContentDto dto) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "POST /models/" + id + "/versions");
        return consentService.createVersion(id, dto.getLocale(), dto.getContent());
    }

    @GET
    @Path("/{id}/versions/latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestVersion(@PathParam("id") @Valid @UUID String id, @Context UriInfo info) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions/latest");
        ModelVersion latest = consentService.findLatestVersionForEntry(id);
        URI uri = info.getBaseUriBuilder().path(ModelsResource.class).path(id).path("versions").path(latest.id).build();
        return Response.status(Response.Status.SEE_OTHER).location(uri).build();
    }

    @GET
    @Path("/{id}/versions/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveVersion(@PathParam("id") @Valid @UUID String id, @Context UriInfo info) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions/active");
        ModelVersion active = consentService.findActiveVersionForEntry(id);
        URI uri = info.getBaseUriBuilder().path(ModelsResource.class).path(id).path("versions").path(active.id).build();
        return Response.status(Response.Status.SEE_OTHER).location(uri).build();
    }

    @GET
    @Path("/{id}/versions/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersion getVersion(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid) throws EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /models/" + id + "/versions/" + vid);
        return consentService.getVersion(vid);
    }

    @PUT
    @Path("/{id}/versions/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersion updateVersion(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid, ContentDto dto) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "PUT /models/" + id + "/versions/" + vid);
        return consentService.updateVersion(vid, dto.getLocale(), dto.getContent());
    }

    @PUT
    @Path("/{id}/versions/{vid}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersion updateVersionStatus(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid, ModelVersion.Status status) throws EntityNotFoundException, ConsentManagerException, InvalidStatusException {
        LOGGER.log(Level.INFO, "PUT /models/" + id + "/versions/" + vid + "/status");
        return consentService.updateVersionStatus(vid, status);
    }

    @PUT
    @Path("/{id}/versions/{vid}/type")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelVersion updateVersionType(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid, ModelVersion.Type type) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "PUT /models/" + id + "/versions/" + vid + "/type");
        return consentService.updateVersionType(vid, type);
    }

    @DELETE
    @Path("/{id}/versions/{vid}")
    public void deleteVersion(@PathParam("id") @Valid @UUID String id, @PathParam("vid") @Valid @UUID String vid) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "DELETE /models/" + id + "/versions/" + vid);
        consentService.deleteVersion(vid);
    }
}
