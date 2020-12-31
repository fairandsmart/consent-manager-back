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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class StatCalendar {

    private static final Logger LOGGER = Logger.getLogger(StatCalendar.class.getName());

    private final SimpleDateFormat FORMAT_DAYS = new SimpleDateFormat("u");
    private final SimpleDateFormat FORMAT_WEEKS = new SimpleDateFormat("w");
    private final SimpleDateFormat FORMAT_MONTHS = new SimpleDateFormat("M");

    private final TimeScale timeScale;
    private final Calendar calendar;
    private final int calendarScale;
    private final int size;

    public enum TimeScale {
        DAYS,
        WEEKS,
        MONTHS
    }

    public StatCalendar(TimeScale timeScale) {
        this(timeScale, System.currentTimeMillis());
    }

    public StatCalendar(TimeScale timeScale, long start) {
        this.timeScale = timeScale;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);
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
    }

    public int getSize() {
        return size;
    }

    public void reset() {
        calendar.add(calendarScale, -size);
    }

    public void forward() {
        calendar.add(calendarScale, 1);
    }

    public void backward() {
        calendar.add(calendarScale, -1);
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    public String getTimeAsString() {
        return new SimpleDateFormat("E dd-MM-yyyy").format(calendar.getTime());
    }

    public String formatDateForLabel() {
        Date time = calendar.getTime();
        String formatted;
        switch (timeScale) {
            case DAYS:
                formatted = FORMAT_DAYS.format(time);
                break;
            case WEEKS:
                formatted = FORMAT_WEEKS.format(time);
                break;
            case MONTHS:
            default:
                formatted = FORMAT_MONTHS.format(time);
                break;
        }
        return formatted;
    }
}
