package com.dex.mobassist.server.util;

import lombok.NonNull;

import java.util.Calendar;
import java.util.Date;

public final class Dates {
    public static Date yesterday() {
        return daysFromToday(-1);
    }
    public static Date today() {
        return daysFromToday(0);
    }
    public static Date tomorrow() {
        return daysFromToday(1);
    }

    public static Date daysFromToday(int days) {
        final Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return daysFromDate(cal, days);
    }

    public static Date daysFromDate(@NonNull Date date, int days) {
        final Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return daysFromDate(cal, days);
    }

    public static Date daysFromDate(@NonNull Calendar cal, int days) {
        cal.add(Calendar.DAY_OF_YEAR, days);

        return cal.getTime();
    }

    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }

        return date.compareTo(today()) >= 0 && date.compareTo(tomorrow()) < 0;
    }
}
