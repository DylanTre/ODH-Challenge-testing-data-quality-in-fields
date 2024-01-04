package it.unibz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class DateUtils {

    public static final String ACCEPTED_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String ACCEPTED_FORMAT_2 = "dd/MM/yyyy HH:mm:ss";
    public static final String ACCEPTED_FORMAT_3 = "dd.MM.yyyy HH:mm:ss";

    public static LocalDateTime parseDate(String input) {
        // DateTimeFormatter with optional sections for different date formats
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .optionalStart()
                .append(DateTimeFormatter.ISO_DATE_TIME)
                .optionalEnd()
                .optionalStart()
                .appendPattern(ACCEPTED_FORMAT_1)
                .optionalEnd()
                .optionalStart()
                .appendPattern(ACCEPTED_FORMAT_2)
                .optionalEnd()
                .optionalStart()
                .appendPattern(ACCEPTED_FORMAT_3)
                .optionalEnd()
                .toFormatter();

        try {
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
