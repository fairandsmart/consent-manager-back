package com.fairandsmart.consent.stats;

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

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.cache.StatisticsCache;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.entity.Subject;
import com.fairandsmart.consent.manager.model.Conditions;
import com.fairandsmart.consent.manager.model.Preference;
import com.fairandsmart.consent.manager.model.Processing;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.stats.dto.*;
import com.fairandsmart.consent.stats.util.StatCalendar;
import io.quarkus.scheduler.Scheduled;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class StatisticsServiceBean implements StatisticsService {

    private static final Logger LOGGER = Logger.getLogger(StatisticsService.class.getName());

    @Inject
    AuthenticationService authentication;

    @Inject
    StatisticsCache cache;

    @Override
    public StatsBag get() throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Fetching stats");
        this.refresh();
        if (!authentication.isConnectedIdentifierAdmin()) {
            throw new AccessDeniedException("You must be admin to search stats");
        }

        final List<String> entriesTypes = Arrays.asList(Processing.TYPE, Preference.TYPE, Conditions.TYPE);
        StatsBag bag = new StatsBag();

        StatsChart modelsChart = new StatsChart();
        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            modelsChart.put(scale.name(), getStats(scale, entriesTypes, "models"));
        }
        bag.put("models", modelsChart);

        StatsChart recordsChart = new StatsChart();
        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            recordsChart.put(scale.name(), getStats(scale, entriesTypes, "records"));
        }
        bag.put("records", recordsChart);

        StatsChart subjectsChart = new StatsChart();
        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            subjectsChart.put(scale.name(), getStats(scale, Collections.singletonList("subjects"), "subjects"));
        }
        bag.put("subjects", subjectsChart);

        StatsChart totalChart = new StatsChart();
        totalChart.put("models", getTotalStats(entriesTypes, "models"));
        totalChart.put("records", getTotalStats(entriesTypes, "records"));
        totalChart.put("subjects", getSubjectsTotalStats());
        bag.put("total", totalChart);

        StatsChart topChart = new StatsChart();
        topChart.put("collected", getTopStats("collected", null));
        topChart.put("accepted", getTopStats("accepted", "accepted"));
        bag.put("top", topChart);

        return bag;
    }

    @Override
    @Scheduled(cron = "0 0 0 ? * *")
    public void init() {
        LOGGER.info("Initializing all stats");
        final List<String> entriesTypes = Arrays.asList(Processing.TYPE, Preference.TYPE, Conditions.TYPE);

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new StatisticsServiceBean.ModelsStatsBuilder().build(scale, scale.getDefaultSize(), entriesTypes, cache, "models");
        }

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new StatisticsServiceBean.RecordsStatsBuilder().build(scale, scale.getDefaultSize(), entriesTypes, cache, "records");
        }

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new StatisticsServiceBean.SubjectsStatsBuilder().build(scale, scale.getDefaultSize(), Collections.singletonList("subjects"), cache, "subjects");
        }
    }

    public void refresh() {
        LOGGER.info("Refresh current day stats");
        final List<String> entriesTypes = Arrays.asList(Processing.TYPE, Preference.TYPE, Conditions.TYPE);

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new StatisticsServiceBean.ModelsStatsBuilder().build(scale, 1, entriesTypes, cache, "models");
        }

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new StatisticsServiceBean.RecordsStatsBuilder().build(scale, 1, entriesTypes, cache, "records");
        }

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new StatisticsServiceBean.SubjectsStatsBuilder().build(scale, 1, Collections.singletonList("subjects"), cache, "subjects");
        }
    }

    private Long read(String key) {
        if (cache.containsKey(key)) {
            return cache.lookup(key);
        } else {
            return 0L;
        }
    }

    private static String formatKey(List<String> parts) {
        return String.join("/", parts);
    }

    private abstract static class StatsBuilder {

        abstract public long compute(String param, long after, long before);

        public void build(StatCalendar.TimeScale timeScale, int size, List<String> collection, StatisticsCache cache, String prefix) {
            StatCalendar calendar = new StatCalendar(timeScale, size);
            for (String element : collection) {
                calendar.reset();
                cache.put(String.join("/", Arrays.asList(prefix, element, "total")), compute(element, -1, -1));
                for (int i = 0; i < calendar.getSize(); i++) {
                    StatCalendar.StatInterval interval = calendar.nextInterval();
                    cache.put(formatKey(Arrays.asList(prefix, element, timeScale.name(), interval.label)), compute(element, interval.after, interval.before));
                }
            }
            cache.put(formatKey(Arrays.asList(prefix, "all", timeScale.name())), compute(null, calendar.getTimeInMillis(), -1));
            cache.put(String.join("/", Arrays.asList(prefix, "total")), compute(null, -1, -1));
        }
    }

    private static class ModelsStatsBuilder extends StatisticsServiceBean.StatsBuilder {
        @Override
        public long compute(String param, long after, long before) {
            List<String> queryParts = new ArrayList<>();
            Map<String, Object> paramsMap = new HashMap<>();
            if (after >= 0) {
                queryParts.add("creationDate >= :after");
                paramsMap.put("after", after);
            }
            if (before >= 0) {
                queryParts.add("creationDate < :before");
                paramsMap.put("before", before);
            }
            queryParts.add("status = :status");
            paramsMap.put("status", ModelVersion.Status.ACTIVE);
            if (param != null) {
                queryParts.add("entry.type = :type");
                paramsMap.put("type", param);
            }
            return ModelVersion.count(String.join(" and ", queryParts), paramsMap);
        }
    }

    private static class RecordsStatsBuilder extends StatisticsServiceBean.StatsBuilder {
        @Override
        public long compute(String param, long after, long before) {
            List<String> queryParts = new ArrayList<>();
            Map<String, Object> paramsMap = new HashMap<>();
            if (after >= 0) {
                queryParts.add("creationTimestamp >= :after");
                paramsMap.put("after", after);
            }
            if (before >= 0) {
                queryParts.add("creationTimestamp < :before");
                paramsMap.put("before", before);
            }
            queryParts.add("state = :state");
            paramsMap.put("state", Record.State.COMMITTED);
            if (param != null) {
                queryParts.add("type = :type");
                paramsMap.put("type", param);
            }
            return Record.count(String.join(" and ", queryParts), paramsMap);
        }
    }

    private static class SubjectsStatsBuilder extends StatisticsServiceBean.StatsBuilder {
        @Override
        public long compute(String param, long after, long before) {
            if (before >= 0) {
                return Subject.count("creationTimestamp < ?1", before);
            } else {
                return Subject.count();
            }
        }
    }

    private StatsData getStats(StatCalendar.TimeScale timeScale, List<String> collection, String prefix) {
        StatsData data = new StatsData();
        List<String> labels = new ArrayList<>();
        StatCalendar calendar = new StatCalendar(timeScale, timeScale.getDefaultSize());

        calendar.reset();
        for (int i = 0; i < calendar.getSize(); i++) {
            String timeLabel = calendar.nextInterval().label;
            labels.add(timeLabel);
        }
        data.setLabels(labels);

        for (String element : collection) {
            StatsDataSet<Long> dataSet = new StatsDataSet<>();
            dataSet.setLabel(element);
            calendar.reset();
            for (int i = 0; i < calendar.getSize(); i++) {
                String timeLabel = calendar.nextInterval().label;
                dataSet.addData(this.read(String.join("/", Arrays.asList(prefix, element, timeScale.name(), timeLabel))));
            }
            data.addDataset(dataSet);
        }
        return data;
    }

    private StatsData getTotalStats(List<String> collection, String prefix) {
        StatCalendar.TimeScale evolutionScale = StatCalendar.TimeScale.MONTHS;
        StatsData data = new StatsData();
        data.setLabels(Arrays.asList("total", "evolution"));
        for (String element : collection) {
            StatsDataSet<Long> dataSet = new StatsDataSet<>();
            dataSet.setLabel(element);
            dataSet.addData(this.read(String.join("/", Arrays.asList(prefix, element, "total"))));
            dataSet.addData(this.read(String.join("/", Arrays.asList(prefix, element, evolutionScale.name(),
                    new StatCalendar(evolutionScale, evolutionScale.getDefaultSize()).formatDateForLabel()))));
            data.addDataset(dataSet);
        }
        StatsDataSet<Long> dataSet = new StatsDataSet<>();
        dataSet.setLabel("all");
        dataSet.addData(this.read(String.join("/", Arrays.asList(prefix, "total"))));
        dataSet.addData(this.read(String.join("/", Arrays.asList(prefix, "all", evolutionScale.name()))));
        data.addDataset(dataSet);
        return data;
    }

    private StatsData getSubjectsTotalStats() {
        StatCalendar.TimeScale evolutionScale = StatCalendar.TimeScale.MONTHS;
        StatsData data = new StatsData();
        data.setLabels(Arrays.asList("total", "evolution"));
        StatsDataSet<Long> dataSet = new StatsDataSet<>();
        dataSet.setLabel("subjects");
        dataSet.addData(this.read("subjects/subjects/total"));
        dataSet.addData(Subject.count("creationTimestamp >= ?1", new StatCalendar(evolutionScale, evolutionScale.getDefaultSize()).getTimeInMillis()));
        data.addDataset(dataSet);
        return data;
    }

    private StatsData getTopStats(String label, String value) {
        List<ModelVersion> processing = ModelVersion.find("entry.type = ?1 and status = ?2", Processing.TYPE, ModelVersion.Status.ACTIVE).list();

        List<TopStat> top = new ArrayList<>();
        if (value != null) {
            for (ModelVersion version : processing) {
                top.add(new TopStat(version.entry.name, Record.count("bodyKey = ?1 and value = ?2", version.entry.key, value)));
            }
        } else {
            for (ModelVersion version : processing) {
                top.add(new TopStat(version.entry.name, Record.count("bodyKey = ?1", version.entry.key)));
            }
        }
        top.sort(TopStat::compareDesc);

        StatsData data = new StatsData();
        List<String> labels = new ArrayList<>();
        StatsDataSet<Long> topCollectedDataSet = new StatsDataSet<>();
        topCollectedDataSet.setLabel(label);

        int length = Math.min(3, processing.size());
        for (int i = 0; i < length; i++) {
            labels.add(top.get(i).getName());
            topCollectedDataSet.addData(top.get(i).getValue());
        }

        data.setLabels(labels);
        data.addDataset(topCollectedDataSet);
        return data;
    }

}
