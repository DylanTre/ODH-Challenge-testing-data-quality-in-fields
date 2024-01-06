package it.unibz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing dates represented as strings into {@code LocalDateTime}.
 */
public class DateUtils {

    /**
     * Accepted date-time format: "yyyy-MM-dd HH:mm:ss".
     */
    private static final String ACCEPTED_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * Accepted date-time format: "dd/MM/yyyy HH:mm:ss".
     */
    private static final String ACCEPTED_FORMAT_2 = "dd/MM/yyyy HH:mm:ss";

    /**
     * Accepted date-time format: "dd.MM.yyyy HH:mm:ss".
     */
    private static final String ACCEPTED_FORMAT_3 = "dd.MM.yyyy HH:mm:ss";

    /**
     * Parses a date string into a {@code LocalDateTime} object.
     *
     * @param input The date string to parse.
     * @return A {@code LocalDateTime} representing the parsed date, or {@code null} if parsing fails.
     */
    public static LocalDateTime parseDate(String input) {
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
