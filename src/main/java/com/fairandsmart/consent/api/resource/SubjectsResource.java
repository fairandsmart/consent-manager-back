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

import com.fairandsmart.consent.api.dto.SubjectDto;
import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.manager.ConsentService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("subjects")
@Tag(name = "Subjects", description = "Operations related to subjects")
public class SubjectsResource {

    private static final Logger LOGGER = Logger.getLogger(SubjectsResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The subject has been retrieved"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Search subjects by name")
    public List<SubjectDto> listSubjects(
            @Parameter(description = "Pattern to use, use % as wildcard", example = "a mysterious %") @QueryParam("name") @NotNull @NotEmpty String name
    ) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /subjects");
        return consentService.findSubjects(name).stream().map(SubjectDto::fromSubject).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The subject has been created"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
            @APIResponse(responseCode = "409", description = "A subject with the same name already exists"),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Create new subject")
    public SubjectDto createSubject(
            @Parameter(description = "the subject to create", example = Placeholders.SHELDON) @Valid @NotNull SubjectDto dto
    ) throws ConsentManagerException, EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "POST /subjects");
        return SubjectDto.fromSubject(consentService.createSubject(dto.getName(), dto.getEmailAddress()));
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The subject has been retrieved"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Get subject by name", description = "If subject do not exists, send back an empty")
    public SubjectDto getSubject(
            @Parameter(description = "the subject id", example = Placeholders.SHELDON) @PathParam("name") @NotNull String name
    ) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /subjects/{0}", name);
        return SubjectDto.fromSubject(consentService.getSubject(name));
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The subject has been updated"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
            @APIResponse(responseCode = "404", description = "The subject has not been found"),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Update subject")
    public SubjectDto updateSubject(
            @Parameter(description = "the subject id", example = Placeholders.NIL_UUID) @PathParam("id") @Valid @UUID String id,
            @Valid @NotNull SubjectDto dto
    ) throws AccessDeniedException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "PUT /subjects/{0}", id);
        return SubjectDto.fromSubject(consentService.updateSubject(id, dto.getEmailAddress()));
    }

    @GET
    @Path("{subject}/records")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The subject records have been retrieved"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Get subject records")
    public Response listSubjectRecords(
            @Parameter(description = "the subject id", example = Placeholders.SHELDON) @PathParam("subject") String subject,
            @Context UriInfo uriInfo
    ) {
        LOGGER.log(Level.INFO, "GET /subjects/{0}/records", subject);
        URI other = uriInfo.getBaseUriBuilder().path(RecordsResource.class).queryParam("subject", subject).build();
        return Response.seeOther(other).build();
    }

}
