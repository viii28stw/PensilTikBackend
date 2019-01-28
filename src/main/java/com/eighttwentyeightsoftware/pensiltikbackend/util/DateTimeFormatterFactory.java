package com.eighttwentyeightsoftware.pensiltikbackend.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeFormatterFactory {

    private static final DateTimeFormatter DATE_FORMAT;
    private static final DateTimeFormatter DATE_TIME_FORMATTER;

    static {
        DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
        DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-mm-dd hh:mm:ss");
    }

    private DateTimeFormatterFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static DateTimeFormatter getDateFormat() {
        return DATE_FORMAT;
    }

    public static DateTimeFormatter getDateTimeFormat() {
        return DATE_TIME_FORMATTER;
    }

}
