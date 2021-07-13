package com.fairandsmart.consent.api.dto;

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

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.entity.EventType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Map;

@Schema(description = "Represents an event")
public class EventDto {

    @Schema(description = "The event ID", readOnly = true, example = Placeholders.NIL_UUID)
    private String id;
    @NotEmpty
    @Size(max = 255)
    @Schema(description = "The event author", example = Placeholders.SHELDON)
    private String author;
    @NotEmpty
    @Size(max = 255)
    @Schema(description = "The event type", example = EventType.CONSENT_SUBMIT)
    private String eventType;
    @NotEmpty
    @Schema(description = "The event source type")
    private String sourceType;
    @NotEmpty
    @Schema(description = "The event source id")
    private String sourceId;
    @Email
    @Schema(description = "The event args")
    private Map<String, String> args;

    public static EventDto fromEvent(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setAuthor(event.getAuthor());
        dto.setSourceType(event.getSourceType());
        dto.setSourceId(event.getSourceId());
        dto.setArgs(event.getArgs());
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", eventType='" + eventType + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", args=" + args +
                '}';
    }
}
