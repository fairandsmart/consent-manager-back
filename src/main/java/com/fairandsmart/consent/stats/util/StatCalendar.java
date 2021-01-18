package com.fairandsmart.consent.stats.util;

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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class StatCalendar {

    private final DateTimeFormatter FORMAT_DAYS = DateTimeFormatter.ofPattern("e");
    private final DateTimeFormatter FORMAT_WEEKS = DateTimeFormatter.ofPattern("w");
    private final DateTimeFormatter FORMAT_MONTHS = DateTimeFormatter.ofPattern("M");

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
        calendar.setTimeInMillis(start);
        switch (timeScale) {
            case DAYS:
                calendarScale = Calendar.DAY_OF_YEAR;
                this.size = size;
                break;
            case WEEKS:
                calendarScale = Calendar.WEEK_OF_YEAR;
                this.size = size;
                calendar.set(Calendar.DAY_OF_WEEK, 2);
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
        LocalDateTime date = this.getTimeAsLocalDateTime();
        String formatted;
        switch (timeScale) {
            case DAYS:
                formatted = FORMAT_DAYS.format(date);
                break;
            case WEEKS:
                formatted = FORMAT_WEEKS.format(date);
                break;
            case MONTHS:
            default:
                formatted = FORMAT_MONTHS.format(date);
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
