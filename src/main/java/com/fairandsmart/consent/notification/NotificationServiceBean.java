package com.fairandsmart.consent.notification;

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

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.util.PageUtil;
import com.fairandsmart.consent.common.util.SortUtil;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import com.fairandsmart.consent.notification.filter.EventFilter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.vertx.mutiny.core.eventbus.EventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class NotificationServiceBean implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    @Inject
    EventBus bus;

    @Override
    public void publish(String eventType, String sourceType, String sourceId, String author) {
        Event event = new Event().withEventType(eventType).withAuthor(author).withSourceType(sourceType).withSourceId(sourceId);
        this.publish(event);
    }

    @Override
    public void publish(String eventType, String sourceType, String sourceId, String author, Map<String, String> args) {
        Event event = new Event().withEventType(eventType).withAuthor(author).withSourceType(sourceType).withSourceId(sourceId).withArgs(args);
        this.publish(event);
    }

    @Override
    public void publish(Event event) {
        LOGGER.log(Level.FINE, "Notify");
        event.getChannels().stream().forEach((channel) -> bus.publish((String) channel, event));
    }

    @Override
    @Transactional
    public void pushReport(NotificationReport report) {
        LOGGER.log(Level.FINE, "Push notification report");
        report.persist();
    }

    @Override
    public List<NotificationReport> listReports(String transaction) {
        LOGGER.log(Level.FINE, "Listing notification reports for transaction: " + transaction);
        return NotificationReport.list("transaction = ?1", transaction);
    }

    @Override
    public CollectionPage<Event> findEvents(EventFilter filter) {
        LOGGER.log(Level.FINE, "Listing events");
        PanacheQuery<Event> query;
        Sort sort = SortUtil.fromFilter(filter);
        if (sort != null) {
            query = ModelEntry.find(filter.getQueryString(), sort, filter.getQueryParams());
        } else {
            query = ModelEntry.find(filter.getQueryString(), filter.getQueryParams());
        }
        return PageUtil.paginateQuery(query, filter);
    }
}
