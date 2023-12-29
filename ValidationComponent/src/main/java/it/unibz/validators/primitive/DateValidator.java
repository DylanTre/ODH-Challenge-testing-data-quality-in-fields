package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateValidator extends AbstractValidator<LocalDateTime> {

    private static final String DATE_VALIDATOR_KEY = "date";

    private static final String RULE_FORMAT_VIOLATION = "Date %s format invalid. Must be %s";
    private static final String RULE_DAY_OF_WEEK_VIOLATION = "Date %s day of week invalid. Must be %s";
    private static final String RULE_IS_BEFORE_VIOLATION = "Date %s is not before date %s";
    private static final String RULE_IS_AFTER_VIOLATION = "Date %s is not after date %s";

    private static final String DEFAULT_ITALIAN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final Locale ROOT_LOCALE = Locale.ROOT;

    private final ArrayNode violationMessages;
    private final ObjectNode dateViolations;

    public DateValidator(JsonNode validationRules) {
        super(validationRules);

        this.violationMessages = getObjectMapper().createArrayNode();
        this.dateViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(String key, JsonNode inputValue) {
        /*
         * FIXME:
         *  Improve validation logic and
         *  validation Scenarios
         *  ------------------------------------------------
         *  Always parse the date first when checking format
         *  only then can continue to check other date-related constraints
         */

        if (JsonNodeType.STRING != inputValue.getNodeType()) {
            return null;
        }

        String dateTimeValue = inputValue.textValue();

        LocalDateTime parsedDate = parseDate(dateTimeValue, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (parsedDate == null) {
            return null;
        }

        parseJsonObject(key, parsedDate, validationRules);
        dateViolations.put(getValidatorKey(), violationMessages);
        return dateViolations;
    }

    @Override
    public void applySingleRule(String key, LocalDateTime inputValue, String ruleName, JsonNode ruleValue) {
        boolean valid;
        String constrainDateStringValue = ruleValue.textValue();

        switch (ruleName) {
            case "key_match" -> {}
            case "format" -> {
                valid = isFormatValid(inputValue, constrainDateStringValue);
                checkForViolation(valid, String.format(RULE_FORMAT_VIOLATION, inputValue, constrainDateStringValue));
            }
            case "day_of_week" -> {
                valid = isDateOfWeekValid(inputValue, DayOfWeek.valueOf(constrainDateStringValue.toUpperCase(ROOT_LOCALE)));
                checkForViolation(valid, String.format(RULE_DAY_OF_WEEK_VIOLATION, inputValue, constrainDateStringValue));
            }
            case "is_before" -> {
                valid = isDateBeforeValid(inputValue, constrainDateStringValue);
                checkForViolation(valid, String.format(RULE_IS_BEFORE_VIOLATION, inputValue, constrainDateStringValue));
            }
            case "is_after" -> {
                valid = isDateAfterValid(inputValue, constrainDateStringValue);
                checkForViolation(valid, String.format(RULE_IS_AFTER_VIOLATION, inputValue, constrainDateStringValue));
            }
            default -> throw new IllegalArgumentException("Rule " + ruleName + "unrecognized");
        }
    }

    @Override
    public void checkForViolation(boolean valid, String violationMessage) {
        if (!valid) {
            violationMessages.add(DATE_VALIDATOR_KEY);
        }
    }

    @Override
    public String getValidatorKey() {
        return DATE_VALIDATOR_KEY;
    }

    public boolean isFormatValid(String dateString, DateTimeFormatter acceptedFormat) {
        return parseDate(dateString, acceptedFormat) != null;
    }

    public static boolean isFormatValid(LocalDateTime dateTime, String expectedFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(expectedFormat);
            dateTime.format(formatter);
        } catch (DateTimeException e) {
            return false;
        }

        return true;
    }


    public boolean isDateOfWeekValid(LocalDateTime parsedDate, DayOfWeek dayOfWeek) {
        return parsedDate.getDayOfWeek() == dayOfWeek;
    }

    /*
     * Accepted dates come as a String from a JSON object
     */
    public boolean isDateBeforeValid(LocalDateTime parsedDate, String acceptedBeforeDateString) {
        LocalDateTime parsedBeforeDate = parseDate(acceptedBeforeDateString,
                DateTimeFormatter.ofPattern(DEFAULT_ITALIAN_DATE_TIME_FORMAT));
        return parsedBeforeDate != null && parsedDate.isBefore(parsedBeforeDate);
    }

    public boolean isDateAfterValid(LocalDateTime parsedDate, String acceptedAfterDateString) {
        LocalDateTime parsedAfterDate = parseDate(acceptedAfterDateString,
                DateTimeFormatter.ofPattern(DEFAULT_ITALIAN_DATE_TIME_FORMAT));
        return parsedAfterDate != null && parsedDate.isAfter(parsedAfterDate);
    }

    private LocalDateTime parseDate(String dateToParse, DateTimeFormatter parseFormat) {
        try {
            return LocalDateTime.parse(dateToParse, parseFormat);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}