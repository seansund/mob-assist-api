package com.dex.mobassist.server.exceptions;

public class UnableToParseDate extends RuntimeException {
    public UnableToParseDate(String pattern, String date) {
        super(String.format("Unable to parse date with pattern: %s, %s", pattern, date));
    }
}
