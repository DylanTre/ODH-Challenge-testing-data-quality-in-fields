package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.constants.Configuration;
import it.unibz.constants.ViolationMessage;
import it.unibz.utils.DateUtils;
import it.unibz.validators.AbstractValidator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Validator for date values.
 * <p>
 * Extends {@code AbstractValidator<LocalDateTime>} and provides specific validation
 * logic for local date time values.
 */
public class DateTimeValidator extends AbstractValidator<LocalDateTime> {

    private static final String DATE_VALIDATOR_KEY = "date";

    private final ObjectNode dateViolations;

    public DateTimeValidator(JsonNode validationRules) {
        super(validationRules);

        this.dateViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(String inputKey, JsonNode inputValue) {
        if (JsonNodeType.STRING != inputValue.getNodeType()) {
            return null;
        }

        String dateTimeValue = inputValue.textValue();

        LocalDateTime parsedDate = DateUtils.parseDate(dateTimeValue);
        if (parsedDate == null) {
            return null;
        }

        applyValidationRule(inputKey, parsedDate, validationRules);

        if (violationMessages.isEmpty()) {
            return objectMapper.createObjectNode();
        }

        dateViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return dateViolations;
    }

    @Override
    public void applySpecificValidationRule(String inputKey, LocalDateTime inputValue, String ruleName, JsonNode ruleValue) {
        String constrainDateStringValue = ruleValue.textValue();

        switch (ruleName) {
            case "day_of_week" -> {
                String dayOfWeek = constrainDateStringValue.toUpperCase(Configuration.ROOT_LOCALE);
                checkForViolation(isDateOfWeekValid(inputValue, DayOfWeek.valueOf(dayOfWeek)),
                        ViolationMessage.RULE_DAY_OF_WEEK_VIOLATION,
                        inputKey, inputValue, inputValue.getDayOfWeek(), dayOfWeek);
            }

            case "before" -> checkForViolation(isDateBeforeValid(inputValue, constrainDateStringValue),
                    ViolationMessage.RULE_IS_BEFORE_VIOLATION,
                    inputKey, inputValue, constrainDateStringValue);

            case "after" -> checkForViolation(isDateAfterValid(inputValue, constrainDateStringValue),
                    ViolationMessage.RULE_IS_AFTER_VIOLATION,
                    inputKey, inputValue, constrainDateStringValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    @Override
    public String getValidatorKey() {
        return DATE_VALIDATOR_KEY;
    }

    /**
     * Checks if the day of the week of a parsed date matches the specified day of the week.
     * <p>
     * This method compares the day of the week of the provided parsed date with the specified
     * day of the week to determine if they are equal.
     *
     * @param parsedDate The parsed date to check the day of the week.
     * @param dayOfWeek  The expected day of the week for comparison.
     * @return {@code true} if the day of the week of the parsed date matches the expected day of the week, {@code false} otherwise.
     */
    private boolean isDateOfWeekValid(LocalDateTime parsedDate, DayOfWeek dayOfWeek) {
        return parsedDate.getDayOfWeek() == dayOfWeek;
    }

    /**
     * Checks if a parsed date is before a specified accepted date.
     * <p>
     * This method compares the provided parsed date with the parsed date of the accepted
     * before date to determine if the parsed date is before the accepted date.
     *
     * @param parsedDate               The parsed date to be checked.
     * @param acceptedBeforeDateString The string representation of the accepted before date.
     * @return {@code true} if the parsed date is before the accepted date, {@code false} otherwise.
     */
    private boolean isDateBeforeValid(LocalDateTime parsedDate, String acceptedBeforeDateString) {
        LocalDateTime parsedBeforeDate = DateUtils.parseDate(acceptedBeforeDateString);
        return parsedBeforeDate != null && parsedDate.isBefore(parsedBeforeDate);
    }

    /**
     * Checks if a parsed date is after a specified accepted date.
     * <p>
     * This method compares the provided parsed date with the parsed date of the accepted
     * after date to determine if the parsed date is after the accepted date.
     *
     * @param parsedDate              The parsed date to be checked.
     * @param acceptedAfterDateString The string representation of the accepted after date.
     * @return {@code true} if the parsed date is after the accepted date, {@code false} otherwise.
     */
    private boolean isDateAfterValid(LocalDateTime parsedDate, String acceptedAfterDateString) {
        LocalDateTime parsedAfterDate = DateUtils.parseDate(acceptedAfterDateString);
        return parsedAfterDate != null && parsedDate.isAfter(parsedAfterDate);
    }
}
