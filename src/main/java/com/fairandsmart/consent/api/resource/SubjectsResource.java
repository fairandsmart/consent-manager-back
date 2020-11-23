package com.fairandsmart.consent.api.resource;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.api.dto.ExtractSubjectDto;
import com.fairandsmart.consent.api.dto.RecordDto;
import com.fairandsmart.consent.api.dto.SubjectDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/subjects")
public class SubjectsResource {

    private static final Logger LOGGER = Logger.getLogger(SubjectsResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubjectDto> listSubjects(@QueryParam("name") @NotNull @NotEmpty String name) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /subjects");
        return consentService.findSubjects(name).stream().map(SubjectDto::fromSubject).collect(Collectors.toList());
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubjectDto> extractSubjects(@Valid ExtractSubjectDto dto) throws AccessDeniedException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /subjects");
        return consentService.findSubjectsWithRecords(dto.getKey(), dto.getValue()).stream().map(SubjectDto::fromSubject).collect(Collectors.toList());
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/csv")
    public List<SubjectDto> extractSubjectsCsv(@Valid ExtractSubjectDto dto) throws AccessDeniedException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /subjects");
        return consentService.findSubjectsWithRecords(dto.getKey(), dto.getValue()).stream().map(SubjectDto::fromSubject).collect(Collectors.toList());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public SubjectDto createSubject(SubjectDto subjectDto) throws ConsentManagerException, EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "POST /subjects/");
        return SubjectDto.fromSubject(consentService.createSubject(subjectDto));
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public SubjectDto getSubject(@PathParam("name") @NotNull String name) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /subjects/" + name);
        return SubjectDto.fromSubject(consentService.getSubject(name));
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SubjectDto updateSubject(@PathParam("id") @Valid @UUID String id, SubjectDto subjectDto) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "PUT /subjects/" + id);
        return SubjectDto.fromSubject(consentService.updateSubject(id, subjectDto));
    }

    @GET
    @Path("{subject}/records")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<RecordDto>> listCustomerRecords(@PathParam("subject") String subject) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /subjects/" + subject + "/records");
        Map<String, List<Record>> records = consentService.listSubjectRecords(subject);
        return records.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (e) -> e.getValue().stream().map(RecordDto::fromRecord).collect(Collectors.toList())));
    }

}
