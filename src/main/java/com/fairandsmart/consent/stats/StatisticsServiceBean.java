package com.fairandsmart.consent.stats;

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

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.stats.dto.*;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.Conditions;
import com.fairandsmart.consent.manager.model.Preference;
import com.fairandsmart.consent.manager.model.Processing;
import com.fairandsmart.consent.stats.util.StatCalendar;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class StatisticsServiceBean implements StatisticsService {

    private static final Logger LOGGER = Logger.getLogger(StatisticsService.class.getName());

    @Inject
    StatisticsStore store;

    @Inject
    AuthenticationService authentication;

    @Override
    public StatsBag getStats() throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Fetching stats");
        if (!authentication.isConnectedIdentifierAdmin()) {
            throw new AccessDeniedException("You must be admin to search stats");
        }

        final List<String> entriesTypes = Arrays.asList(Processing.TYPE, Preference.TYPE, Conditions.TYPE);
        StatsBag bag = new StatsBag();

        StatsChart recordsChart = new StatsChart();
        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            recordsChart.put(scale.name(), buildStats(scale, entriesTypes, store, "records"));
        }
        bag.put("records", recordsChart);

        StatsChart subjectsChart = new StatsChart();
        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            subjectsChart.put(scale.name(), buildStats(scale, Collections.singletonList("subjects"), store, "subjects"));
        }
        bag.put("subjects", subjectsChart);

        StatsChart totalChart = new StatsChart();
        totalChart.put("models", buildTotalStats(entriesTypes, store, "models"));
        totalChart.put("records", buildTotalStats(entriesTypes, store, "records"));
        totalChart.put("subjects", buildTotalStats(Collections.singletonList("subjects"), store, "subjects"));
        bag.put("total", totalChart);

        StatsChart topChart = new StatsChart();
        topChart.put("collected", buildTopStats("collected", null));
        topChart.put("accepted", buildTopStats("accepted", "accepted"));
        bag.put("top", topChart);

        return bag;
    }

    private StatsData buildStats(StatCalendar.TimeScale timeScale, List<String> collection, StatisticsStore store, String root) {
        StatsData data = new StatsData();
        List<String> labels = new ArrayList<>();
        StatCalendar calendar = new StatCalendar(timeScale);

        calendar.reset();
        for (int i = 0; i < calendar.getSize(); i++) {
            calendar.forward();
            labels.add(calendar.formatDateForLabel());
        }
        data.setLabels(labels);

        for (String element : collection) {
            StatsDataSet<Long> dataSet = new StatsDataSet<>();
            dataSet.setLabel(element);
            calendar.reset();
            String timeKey;
            for (int i = 0; i < calendar.getSize(); i++) {
                calendar.forward();
                timeKey = calendar.formatDateForLabel();
                dataSet.addData(store.read(String.join("/", Arrays.asList(root, element, timeScale.name(), timeKey))));
            }
            data.addDataset(dataSet);
        }
        return data;
    }

    private StatsData buildTotalStats(List<String> collection, StatisticsStore store, String root) {
        StatCalendar.TimeScale evolutionScale = StatCalendar.TimeScale.MONTHS;
        StatsData data = new StatsData();
        data.setLabels(Arrays.asList("total", "evolution"));
        for (String element : collection) {
            StatsDataSet<Long> dataSet = new StatsDataSet<>();
            dataSet.setLabel(element);
            dataSet.addData(store.read(String.join("/", Arrays.asList(root, element, "total"))));
            dataSet.addData(store.read(String.join("/", Arrays.asList(root, element, evolutionScale.name(), new StatCalendar(evolutionScale).formatDateForLabel()))));
            data.addDataset(dataSet);
        }
        StatsDataSet<Long> dataSet = new StatsDataSet<>();
        dataSet.setLabel("all");
        dataSet.addData(store.read(String.join("/", Arrays.asList(root, "total"))));
        dataSet.addData(store.read(String.join("/", Arrays.asList(root, "all", evolutionScale.name()))));
        data.addDataset(dataSet);
        return data;
    }

    private StatsData buildTopStats(String label, String value) {
        List<ModelVersion> processing = ModelVersion.find("entry.type = ?1 and status = ?2", Processing.TYPE, ModelVersion.Status.ACTIVE).list();

        List<TopStat> top = new ArrayList<>();
        if (value != null) {
            for (ModelVersion version : processing) {
                top.add(new TopStat(version.entry.key, Record.count("bodyKey = ?1 and value = ?2", version.entry.key, value)));
            }
        } else {
            for (ModelVersion version : processing) {
                top.add(new TopStat(version.entry.key, Record.count("bodyKey = ?1", version.entry.key)));
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
