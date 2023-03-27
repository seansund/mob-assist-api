package com.dex.mobassist.server.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatesTest {
    @Nested
    @DisplayName("Given isToday()")
    class GivenIsToday {
        @Nested
        @DisplayName("when input is yesterday")
        class WhenInputIsYesterday {
            @Test
            @DisplayName("then return false")
            void thenReturnFalse() {
                final Date yesterday = Dates.daysFromDate(new Date(), -1);

                assertFalse(Dates.isToday(yesterday), "Yesterday is not today");
            }
        }
        @Nested
        @DisplayName("when input is tomorrow")
        class WhenInputIsTomorrow {
            @Test
            @DisplayName("then return false")
            void thenReturnFalse() {
                final Date yesterday = Dates.daysFromDate(new Date(), 1);

                assertFalse(Dates.isToday(yesterday), "Tomorrow is not today");
            }
        }
        @Nested
        @DisplayName("when input is now")
        class WhenInputIsNow {
            @Test
            @DisplayName("then return true")
            void thenReturnTrue() {
                final Date now = new Date();

                assertTrue(Dates.isToday(now), "Now is today");
            }
        }
        @Nested
        @DisplayName("when input is now")
        class WhenInputIsMidnightToday {
            @Test
            @DisplayName("then return true")
            void thenReturnTrue() {
                final Calendar thisMorning = Calendar.getInstance();

                thisMorning.set(Calendar.HOUR_OF_DAY, 0);
                thisMorning.set(Calendar.MINUTE, 0);
                thisMorning.set(Calendar.MILLISECOND, 0);

                assertTrue(Dates.isToday(thisMorning.getTime()), "Midnight today is today: ");
            }
        }
        @Nested
        @DisplayName("when input is null")
        class WhenInputIsNull {
            @Test
            @DisplayName("then return false")
            void thenReturnFalse() {
                assertFalse(Dates.isToday(null));
            }
        }
    }
}
