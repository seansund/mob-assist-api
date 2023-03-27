package com.dex.mobassist.server.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringsTest {
    @Nested
    @DisplayName("Given compare()")
    class GivenCompare {
        @Nested
        @DisplayName("when both values are null")
        class WhenBothValuesAreNull {
            @Test
            @DisplayName("then return 0")
            void thenReturn0() {
                assertEquals(0, Strings.compare(null, null), "Null values are equal");
            }
        }
        @Nested
        @DisplayName("when first value is null")
        class WhenFirstValueIsNull {
            @Test
            @DisplayName("then return -1")
            void thenReturnNeg1() {
                assertEquals(-1, Strings.compare(null, "b"));
            }
        }
        @Nested
        @DisplayName("when second value is null")
        class WhenSecondValueIsNull {
            @Test
            @DisplayName("then return 1")
            void thenReturn1() {
                assertEquals(1, Strings.compare("a", null));
            }
        }
        @Nested
        @DisplayName("when both values not null")
        class WhenBothValuesNotNull {
            @Test
            @DisplayName("then return comparison")
            void thenReturnComparison() {
                final String a = "a";
                final String b = "b";

                assertEquals(a.compareTo(b), Strings.compare(a, b));
            }
        }
    }
}
