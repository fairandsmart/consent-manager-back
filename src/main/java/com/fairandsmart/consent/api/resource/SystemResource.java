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
import com.fairandsmart.consent.common.config.ClientConfig;
import com.fairandsmart.consent.common.config.SecurityConfig;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.notification.NotificationService;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.filter.EventFilter;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.support.SupportService;
import com.fairandsmart.consent.support.SupportServiceException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/system")
@Tag(name = "System", description = "System operations")
public class SystemResource {

    private static final Logger LOGGER = Logger.getLogger(SystemResource.class.getName());

    @Inject
    SupportService supportService;

    @Inject
    NotificationService notificationService;

    @Inject
    ConsentService consentService;

    @Inject
    ClientConfig config;

    @Inject
    SecurityConfig securityConfig;

    @GET
    @Path("/support/infos")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Support service is disabled"),
            @APIResponse(responseCode = "200", description = "Support service information")})
    @Operation(summary = "Get available service information (for now, only latest version is operational)")
    public SupportInfoDto supportInfos() throws SupportServiceException {
        LOGGER.log(Level.INFO, "GET /system/support/infos");
        SupportInfoDto dto = new SupportInfoDto();
        dto.setStatus(supportService.getSupportStatus());
        dto.setLatestVersion(supportService.getLatestVersion());
        dto.setCurrentVersion(supportService.getCurrentVersion());
        return dto;
    }

    @GET
    @Path("/config")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Configuration for GUI")
    @Operation(summary = "Get the GUI configuration")
    public ClientConfigDto getClientConfig() {
        LOGGER.log(Level.INFO, "GET /system/config");
        ClientConfigDto dto = ClientConfigDto.fromClientConfig(config);
        dto.setLanguage(supportService.getInstance().language);
        dto.addSecurityRole("admin", securityConfig.adminRoleName());
        dto.addSecurityRole("operator", securityConfig.operatorRoleName());
        dto.addSecurityRole("api", securityConfig.apiRoleName());
        return dto;
    }

    @GET
    @Path("/counters/transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Transaction counter")
    @Operation(summary = "Get the Transaction count between timestamp interval")
    public CounterDto getTransactionCounter(
            @Parameter(description = "timestamp when to start counting") @QueryParam("from") @DefaultValue("0") long from,
            @Parameter(description = "timestamp when to stop counting") @QueryParam("to") @NotEmpty long to
            ) {
        LOGGER.log(Level.INFO, "GET /counters/transaction");
        long value = consentService.countTransactionsCreatedBetween(from, to);
        CounterDto dto = new CounterDto().withFromTimestamp(from).withToTimestamp(to).withValue(value);
        return dto;
    }

    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "System events list")
    @Operation(summary = "Get the GUI configuration")
    public CollectionPage<EventDto> listEvents(
            @Parameter(description = "page number to get") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "page size") @QueryParam("size") @DefaultValue("25") int size,
            @Parameter(description = "sort by") @QueryParam("order") @DefaultValue("key") String order,
            @Parameter(description = "sort direction (asc/desc)") @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction,
            @Parameter(description = "event author to query") @QueryParam("author") String author,
            @Parameter(description = "event types to query") @QueryParam("eventTypes") List<String> eventTypes,
            @Parameter(description = "event source types to query") @QueryParam("sourceTypes") List<String> sourceTypes,
            @Parameter(description = "event source id to query") @QueryParam("sourceId") String sourceId) {
        LOGGER.log(Level.INFO, "GET /system/events");
        EventFilter filter = new EventFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setOrder(order);
        filter.setDirection(direction);
        filter.setAuthor(author);
        filter.setEventTypes(eventTypes);
        filter.setSourceTypes(sourceTypes);
        filter.setSourceId(sourceId);
        CollectionPage<Event> events = notificationService.findEvents(filter);
        CollectionPage<EventDto> dto = new CollectionPage<>(events);
        dto.setValues(events.getValues().stream().map(EventDto::fromEvent).collect(Collectors.toList()));
        return dto;
    }
}
