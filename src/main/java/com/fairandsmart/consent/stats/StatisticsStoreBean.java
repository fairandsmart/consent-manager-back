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

import com.fairandsmart.consent.manager.cache.StatisticsCache;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.entity.Subject;
import com.fairandsmart.consent.manager.model.Conditions;
import com.fairandsmart.consent.manager.model.Preference;
import com.fairandsmart.consent.manager.model.Processing;
import com.fairandsmart.consent.stats.util.StatCalendar;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.logging.Logger;

@Singleton
@Startup
public class StatisticsStoreBean implements StatisticsStore {

    private static final Logger LOGGER = Logger.getLogger(StatisticsStoreBean.class.getName());

    @Inject
    StatisticsCache cache;

    @Override
    @Scheduled(cron="0 0 0 ? * *")
    public void initStats() {
        LOGGER.info("Initializing stats");
        final List<String> entriesTypes = Arrays.asList(Processing.TYPE, Preference.TYPE, Conditions.TYPE);

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new ModelsStatsBuilder().build(scale, entriesTypes, cache, "models");
        }

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new RecordsStatsBuilder().build(scale, entriesTypes, cache, "records");
        }

        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            new SubjectsStatsBuilder().build(scale, Collections.singletonList("subjects"), cache, "subjects");
        }
    }

    @Override
    public void add(String root, String type) {
        LOGGER.info("Incrementing stats : " + root + " " + type);
        update(formatKey(Arrays.asList(root, "total")), 1);
        update(formatKey(Arrays.asList(root, type, "total")), 1);
        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            String dateLabel = new StatCalendar(scale).formatDateForLabel();
            update(formatKey(Arrays.asList(root, "all", scale.name(), dateLabel)), 1);
            update(formatKey(Arrays.asList(root, type, scale.name(), dateLabel)), 1);
        }
    }

    @Override
    public void remove(String root, String type, long timestamp) {
        LOGGER.info("Decrementing stats : " + root + " " + type);
        update(formatKey(Arrays.asList(root, "total")), -1);
        update(formatKey(Arrays.asList(root, type, "total")), -1);
        for (StatCalendar.TimeScale scale : StatCalendar.TimeScale.values()) {
            StatCalendar creationCalendar = new StatCalendar(scale, timestamp);
            StatCalendar limitCalendar = new StatCalendar(scale);
            limitCalendar.reset();
            // TODO problem : ModelVersion statuses are not necessarily set to ACTIVE on their creation day
            if (creationCalendar.getTimeInMillis() >= limitCalendar.getTimeInMillis()) {
                update(formatKey(Arrays.asList(root, "all", scale.name(), creationCalendar.formatDateForLabel())), -1);
                update(formatKey(Arrays.asList(root, type, scale.name(), creationCalendar.formatDateForLabel())), -1);
            }
        }
    }

    @Override
    public Long read(String key) {
        if (cache.containsKey(key)) {
            return cache.lookup(key);
        } else {
            return 0L;
        }
    }

    private void update(String key, int value) {
        Long previous;
        if (cache.containsKey(key)) {
            previous = cache.lookup(key);
        } else {
            previous = 0L;
        }
        cache.put(key, previous + value);
    }

    private static String formatKey(List<String> parts) {
        return String.join("/", parts);
    }

    private abstract static class StatsBuilder {
        abstract public long compute(String param, long after, long before);

        public void build(StatCalendar.TimeScale timeScale, List<String> collection, StatisticsCache cache, String root) {
            StatCalendar calendar = new StatCalendar(timeScale);

            for (String element : collection) {
                calendar.reset();
                cache.put(String.join("/", Arrays.asList(root, element, "total")), compute(element, -1, -1));
                for (int i = 0; i < calendar.getSize(); i++) {
                    String timeKey;
                    long after;
                    long before;
                    calendar.forward();
                    after = calendar.getTimeInMillis();
                    timeKey = calendar.formatDateForLabel();
                    calendar.forward();
                    before = calendar.getTimeInMillis();
                    calendar.backward();
                    cache.put(formatKey(Arrays.asList(root, element, timeScale.name(), timeKey)), compute(element, after, before));
                }
            }
            cache.put(formatKey(Arrays.asList(root, "all", timeScale.name())), compute(null, calendar.getTimeInMillis(), -1));
            cache.put(String.join("/", Arrays.asList(root, "total")), compute(null, -1, -1));
        }
    }

    private static class ModelsStatsBuilder extends StatsBuilder {
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

    private static class RecordsStatsBuilder extends StatsBuilder {
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

    private static class SubjectsStatsBuilder extends StatsBuilder {
        @Override
        public long compute(String param, long after, long before) {
            if (before >= 0) {
                return Subject.count("creationTimestamp < ?1", before);
            } else {
                return Subject.count();
            }
        }
    }

}
