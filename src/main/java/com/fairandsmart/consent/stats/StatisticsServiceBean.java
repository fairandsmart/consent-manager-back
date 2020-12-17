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
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.entity.Subject;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.stats.dto.*;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.Conditions;
import com.fairandsmart.consent.manager.model.Preference;
import com.fairandsmart.consent.manager.model.Processing;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class StatisticsServiceBean implements StatisticsService {

    private static final Logger LOGGER = Logger.getLogger(StatisticsService.class.getName());

    private static final SimpleDateFormat FORMAT_DAYS = new SimpleDateFormat("u");
    private static final SimpleDateFormat FORMAT_WEEKS = new SimpleDateFormat("w");
    private static final SimpleDateFormat FORMAT_MONTHS = new SimpleDateFormat("M");

    private static final List<String> ENTRIES_TYPES = Arrays.asList(Processing.TYPE, Preference.TYPE, Conditions.TYPE);

    @Inject
    AuthenticationService authentication;

    public enum TimeScale {
        DAYS,
        WEEKS,
        MONTHS
    }

    @Override
    public StatsBag getStats() throws AccessDeniedException {
        LOGGER.log(Level.INFO, "Fetching stats");
        if (!authentication.isConnectedIdentifierAdmin()) {
            throw new AccessDeniedException("You must be admin to search stats");
        }

        StatsBag bag = new StatsBag();

        StatsChart modelsChart = new StatsChart();
        for (TimeScale scale : TimeScale.values()) {
            modelsChart.put(scale.name(), new ModelsStatsBuilder().buildStats(scale, ENTRIES_TYPES));
        }
        bag.put("models", modelsChart);

        StatsChart recordsWebformChart = new StatsChart();
        for (TimeScale scale : TimeScale.values()) {
            recordsWebformChart.put(scale.name(), new RecordsStatsBuilder(ConsentContext.CollectionMethod.WEBFORM).buildStats(scale, ENTRIES_TYPES));
        }
        bag.put("recordsWebform", recordsWebformChart);

        StatsChart recordsOperatorChart = new StatsChart();
        for (TimeScale scale : TimeScale.values()) {
            recordsOperatorChart.put(scale.name(), new RecordsStatsBuilder(ConsentContext.CollectionMethod.OPERATOR).buildStats(scale, ENTRIES_TYPES));
        }
        bag.put("recordsOperator", recordsOperatorChart);

        StatsChart subjectsChart = new StatsChart();
        for (TimeScale scale : TimeScale.values()) {
            subjectsChart.put(scale.name(), new SubjectsStatsBuilder().buildStats(scale, Collections.singletonList("subjects")));
        }
        bag.put("subjects", subjectsChart);

        StatsChart totalChart = new StatsChart();
        totalChart.put("models", new ModelsStatsBuilder().buildTotalStats(ENTRIES_TYPES));
        totalChart.put("records", new RecordsStatsBuilder().buildTotalStats(ENTRIES_TYPES));
        totalChart.put("subjects", new SubjectsStatsBuilder().buildTotalStats(Collections.singletonList("subjects")));
        bag.put("total", totalChart);

        StatsChart topChart = new StatsChart();
        topChart.put("topCollectedProcessing", new RecordsStatsBuilder().getTopCollected(3));
        topChart.put("topAcceptedProcessing", new RecordsStatsBuilder().getTopAccepted(3));
        bag.put("top", topChart);

        return bag;
    }

    private abstract static class StatsBuilder {
        abstract public long compute(String param, long after, long before);

        protected StatsBuilder() {
        }

        public StatsData buildStats(TimeScale timeScale, List<String> collection) {
            StatsData data = new StatsData();
            List<String> labels = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            int calendarScale;
            int size;

            switch (timeScale) {
                case DAYS:
                    calendarScale = Calendar.DAY_OF_YEAR;
                    size = 7;
                    break;
                case WEEKS:
                    calendarScale = Calendar.WEEK_OF_YEAR;
                    size = 10;
                    calendar.set(Calendar.DAY_OF_WEEK, 2);
                    break;
                case MONTHS:
                default:
                    calendarScale = Calendar.MONTH;
                    size = 12;
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    break;
            }
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendar.add(calendarScale, -size);
            for (int i = 0; i < size; i++) {
                calendar.add(calendarScale, 1);
                labels.add(this.formatDateForLabel(timeScale, calendar));
            }
            data.setLabels(labels);

            for (String element : collection) {
                StatsDataSet<Long> dataSet = new StatsDataSet<>();
                dataSet.setLabel(element);
                calendar.add(calendarScale, -size);
                long after;
                long before;
                for (int i = 0; i < size; i++) {
                    calendar.add(calendarScale, 1);
                    after = calendar.getTimeInMillis();
                    calendar.add(calendarScale, 1);
                    before = calendar.getTimeInMillis();
                    calendar.add(calendarScale, -1);
                    dataSet.addData(compute(element, after, before));
                }
                data.addDataset(dataSet);
            }
            return data;
        }

        public StatsData buildTotalStats(List<String> collection) {
            StatsData data = new StatsData();
            data.setLabels(Collections.singletonList("total"));
            for (String element : collection) {
                StatsDataSet<Long> dataSet = new StatsDataSet<>();
                dataSet.setLabel(element);
                dataSet.addData(compute(element, 0, Calendar.getInstance().getTimeInMillis()));
                data.addDataset(dataSet);
            }
            return data;
        }

        private String formatDateForLabel(TimeScale timeScale, Calendar calendar) {
            String formatted;
            switch (timeScale) {
                case DAYS:
                    formatted = FORMAT_DAYS.format(calendar.getTime());
                    break;
                case WEEKS:
                    formatted = FORMAT_WEEKS.format(calendar.getTime());
                    break;
                case MONTHS:
                default:
                    formatted = FORMAT_MONTHS.format(calendar.getTime());
                    break;
            }
            return formatted;
        }
    }

    private static class ModelsStatsBuilder extends StatsBuilder {
        @Override
        public long compute(String param, long after, long before) {
            List<String> queryParts = new ArrayList<>();
            Map<String, Object> paramsMap = new HashMap<>();
            queryParts.add("creationDate >= :after");
            paramsMap.put("after", after);
            queryParts.add("creationDate < :before");
            paramsMap.put("before", before);
            queryParts.add("status = :status");
            paramsMap.put("status", ModelVersion.Status.ACTIVE);
            if (param != null) {
                queryParts.add("entry.type = :type");
                paramsMap.put("type", param);
            }
            return ModelVersion.count(String.join(" and ", queryParts), paramsMap);
        }
    }

    private static class RecordsStatsBuilder extends StatsBuilder {
        private ConsentContext.CollectionMethod collectionMethod;

        public RecordsStatsBuilder() {
        }

        public RecordsStatsBuilder(ConsentContext.CollectionMethod collectionMethod) {
            this.collectionMethod = collectionMethod;
        }

        @Override
        public long compute(String param, long after, long before) {
            List<String> queryParts = new ArrayList<>();
            Map<String, Object> paramsMap = new HashMap<>();
            queryParts.add("creationTimestamp >= :after");
            paramsMap.put("after", after);
            queryParts.add("creationTimestamp < :before");
            paramsMap.put("before", before);
            queryParts.add("state = :state");
            paramsMap.put("state", Record.State.COMMITTED);
            if (collectionMethod != null) {
                queryParts.add("collectionMethod = :collectionMethod");
                paramsMap.put("collectionMethod", collectionMethod);
            }
            if (param != null) {
                queryParts.add("type = :type");
                paramsMap.put("type", param);
            }
            return Record.count(String.join(" and ", queryParts), paramsMap);
        }

        public StatsData getTopCollected(int length) {
            List<ModelVersion> processing = ModelVersion.find("entry.type = ?1 and status = ?2", Processing.TYPE, ModelVersion.Status.ACTIVE).list();
            List<TopStat> topCollected = new ArrayList<>();
            for (ModelVersion version : processing) {
                topCollected.add(new TopStat(version.entry.key, Record.count("bodyKey = ?1", version.entry.key)));
            }
            return formatTopStats(topCollected, Math.min(length, processing.size()), "collected");
        }

        public StatsData getTopAccepted(int length) {
            List<ModelVersion> processing = ModelVersion.find("entry.type = ?1 and status = ?2", Processing.TYPE, ModelVersion.Status.ACTIVE).list();
            List<TopStat> topAccepted = new ArrayList<>();
            for (ModelVersion version : processing) {
                topAccepted.add(new TopStat(version.entry.key, Record.count("bodyKey = ?1 and value = ?2", version.entry.key, "accepted")));
            }
            return formatTopStats(topAccepted, Math.min(length, processing.size()), "accepted");
        }

        private StatsData formatTopStats(List<TopStat> topStatsList, int length, String label) {
            topStatsList.sort(TopStat::compareDesc);

            StatsData data = new StatsData();
            List<String> labels = new ArrayList<>();
            StatsDataSet<Long> topCollectedDataSet = new StatsDataSet<>();
            topCollectedDataSet.setLabel(label);

            for (int i = 0; i < length; i++) {
                labels.add(topStatsList.get(i).getName());
                topCollectedDataSet.addData(topStatsList.get(i).getValue());
            }

            data.setLabels(labels);
            data.addDataset(topCollectedDataSet);
            return data;
        }
    }

    private static class SubjectsStatsBuilder extends StatsBuilder {
        @Override
        public long compute(String param, long after, long before) {
            List<String> queryParts = new ArrayList<>();
            Map<String, Object> paramsMap = new HashMap<>();
            queryParts.add("creationTimestamp >= :after");
            paramsMap.put("after", after);
            queryParts.add("creationTimestamp < :before");
            paramsMap.put("before", before);
            return Subject.count(String.join(" and ", queryParts), paramsMap);
        }
    }

}
