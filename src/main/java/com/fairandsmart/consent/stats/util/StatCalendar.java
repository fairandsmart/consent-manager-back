package com.fairandsmart.consent.stats.util;

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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class StatCalendar {

    private final TimeScale timeScale;
    private final Calendar calendar;
    private final int calendarScale;
    private final int size;

    public enum TimeScale {
        DAYS(7),
        WEEKS(10),
        MONTHS(12);

        private int defaultSize;

        TimeScale(int defaultSize) {
            this.defaultSize = defaultSize;
        }

        public int getDefaultSize() {
            return defaultSize;
        }

        public void setDefaultSize(int defaultSize) {
            this.defaultSize = defaultSize;
        }
    }

    public StatCalendar(TimeScale timeScale, int size) {
        this(timeScale, size, System.currentTimeMillis());
    }

    public StatCalendar(TimeScale timeScale, int size, long start) {
        this.timeScale = timeScale;
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setTimeInMillis(start);
        switch (timeScale) {
            case DAYS:
                calendarScale = Calendar.DAY_OF_YEAR;
                this.size = size;
                break;
            case WEEKS:
                calendarScale = Calendar.WEEK_OF_YEAR;
                this.size = size;
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                break;
            case MONTHS:
            default:
                calendarScale = Calendar.MONTH;
                this.size = size;
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public int getSize() {
        return size;
    }

    public void reset() {
        calendar.add(calendarScale, -size);
    }

    public StatInterval nextInterval() {
        StatInterval interval = new StatInterval();
        calendar.add(calendarScale, 1);
        interval.after = this.getTimeInMillis();
        interval.label = this.formatDateForLabel();
        calendar.add(calendarScale, 1);
        interval.before = this.getTimeInMillis();
        calendar.add(calendarScale, -1);
        return interval;
    }

    public String getTimeAsString() {
        return DateTimeFormatter.ofPattern("E d-M-y HH:mm:ss").format(this.getTimeAsLocalDateTime());
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    private LocalDateTime getTimeAsLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(calendar.getTimeInMillis()), ZoneId.systemDefault());
    }

    public String formatDateForLabel() {
        String formatted;
        switch (timeScale) {
            case DAYS:
                formatted = this.getTimeAsLocalDateTime().format(DateTimeFormatter.ISO_WEEK_DATE).split("-")[2];
                break;
            case WEEKS:
                formatted = this.getTimeAsLocalDateTime().format(DateTimeFormatter.ISO_WEEK_DATE).split("-")[1].substring(1);
                break;
            case MONTHS:
            default:
                formatted = this.getTimeAsLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE).split("-")[1];
                break;
        }
        return formatted;
    }

    public static class StatInterval {
        public long after;
        public long before;
        public String label;
    }
}
