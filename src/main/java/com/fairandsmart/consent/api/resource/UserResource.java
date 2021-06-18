package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.*;
import com.fairandsmart.consent.common.config.ClientConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fairandsmart.consent.manager.filter.ModelFilter;
import com.fairandsmart.consent.manager.model.BasicInfo;
import com.fairandsmart.consent.security.AuthenticationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("user")
@Tag(name = "User", description = "Connected user resource")
public class UserResource {

    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

    @Inject
    AuthenticationService authenticationService;

    @Inject
    ConsentService consentService;

    @Inject
    ClientConfig config;

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Information for the connected user")
    @Operation(summary = "Get connected user information")
    public UserDto me() {
        LOGGER.log(Level.INFO, "GET /user/me");
        UserDto user = new UserDto();
        user.setUsername(authenticationService.getConnectedIdentifier());
        user.setAdmin(authenticationService.isConnectedIdentifierAdmin());
        user.setOperator(authenticationService.isConnectedIdentifierOperator());
        return user;
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The users status")
    })
    @Operation(summary = "Get connected user records")
    public UserStatusDto status(@Context UriInfo info) throws UnexpectedException, ModelDataSerializationException, EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /user/status");
        UserStatusDto dto = new UserStatusDto();
        dto.setUsername(authenticationService.getConnectedIdentifier());
        ModelFilter infoFilter = new ModelFilter().withTypes(Collections.singletonList(BasicInfo.TYPE)).withStatuses(Collections.singletonList(ModelEntry.Status.ACTIVE));
        CollectionPage<ModelEntry> infosEntries = consentService.listEntries(infoFilter);
        String infoKey = null;
        if (infosEntries.getSize() >= 1) {
            ModelEntryDto infoDto = ModelEntryDto.fromModelEntry(infosEntries.getValues().get(0), Collections.singletonList(consentService.findActiveVersionForEntry(infosEntries.getValues().get(0).id)));
            infoKey = infoDto.getKey();
            dto.setInfos(infoDto);
        }
        //TODO if config for user page elements is empty, return all entries
        if (config.userPageElements().isPresent()) {
            Map<String, Record> records = consentService.systemListValidRecords(dto.getUsername(), infoKey, Arrays.asList(config.userPageElements().get().split(",")));
            dto.setRecords(records.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> RecordDto.fromRecord(e.getValue()))));
            for (String key: config.userPageElements().get().split(",")) {
                ModelEntry entry = consentService.findEntryForKey(key);
                if (entry.status.equals(ModelEntry.Status.ACTIVE)) {
                    ModelEntryDto entryDto = ModelEntryDto.fromModelEntry(entry, Collections.singletonList(consentService.findActiveVersionForEntry(entry.id)));
                    dto.getEntries().add(entryDto);
                }
            }
        }
        return dto;
    }

    @GET
    @Path("records")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The users records")
    })
    @Operation(summary = "Get connected user records")
    public Response records(@Context UriInfo info) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /user/records");
        URI other = info.getBaseUriBuilder().replacePath(null).path(RecordsResource.class).replaceQueryParam("subject", authenticationService.getConnectedIdentifier()).build();
        return Response.seeOther(other).build();
    }

}
