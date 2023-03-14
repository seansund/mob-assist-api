package com.dex.mobassist.server.util;

import java.util.Calendar;
import java.util.Date;

public final class Dates {
    public static Date yesterday() {
        return daysFromToday(-1);
    }

    public static Date daysFromToday(int days) {
        final Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_YEAR, days);

        return cal.getTime();
    }
}
