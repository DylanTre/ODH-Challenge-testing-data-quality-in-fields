package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.configuration.ValidatorConstants;
import it.unibz.utils.DateUtils;
import it.unibz.validators.AbstractValidator;
import it.unibz.violation.ViolationMessage;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateValidator extends AbstractValidator<LocalDateTime> {

    private static final String DATE_VALIDATOR_KEY = "date";

    private final ObjectNode dateViolations;

    public DateValidator(JsonNode validationRules) {
        super(validationRules);

        this.dateViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(String key, JsonNode inputValue) {
        if (JsonNodeType.STRING != inputValue.getNodeType()) {
            return null;
        }

        String dateTimeValue = inputValue.textValue();

        LocalDateTime parsedDate = DateUtils.parseDate(dateTimeValue);
        if (parsedDate == null) {
            return null;
        }

        parseJsonObject(key, parsedDate, validationRules);
        dateViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return dateViolations;
    }

    @Override
    public void applySingleRule(String key, LocalDateTime inputValue, String ruleName, JsonNode ruleValue) {
        String constrainDateStringValue = ruleValue.textValue();

        switch (ruleName) {
            case "key_match" -> {
            }

            case "name_pattern" -> checkForViolation(isValueMatch(key, ruleValue.textValue()),
                    ViolationMessage.RULE_NAME_PATTERN_VIOLATION,
                    key, ruleValue.textValue());

            case "day_of_week" -> {
                String dayOfWeek = constrainDateStringValue.toUpperCase(ValidatorConstants.ROOT_LOCALE);
                checkForViolation(isDateOfWeekValid(inputValue, DayOfWeek.valueOf(dayOfWeek)),
                        ViolationMessage.RULE_DAY_OF_WEEK_VIOLATION,
                        inputValue, inputValue.getDayOfWeek(), dayOfWeek);
            }

            case "before" -> checkForViolation(isDateBeforeValid(inputValue, constrainDateStringValue),
                    ViolationMessage.RULE_IS_BEFORE_VIOLATION,
                    inputValue, constrainDateStringValue);

            case "after" -> checkForViolation(isDateAfterValid(inputValue, constrainDateStringValue),
                    ViolationMessage.RULE_IS_AFTER_VIOLATION,
                    inputValue, constrainDateStringValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    @Override
    public String getValidatorKey() {
        return DATE_VALIDATOR_KEY;
    }

    private boolean isDateOfWeekValid(LocalDateTime parsedDate, DayOfWeek dayOfWeek) {
        return parsedDate.getDayOfWeek() == dayOfWeek;
    }

    /*
     * Accepted dates come as a String from a JSON object
     */
    private boolean isDateBeforeValid(LocalDateTime parsedDate, String acceptedBeforeDateString) {
        LocalDateTime parsedBeforeDate = DateUtils.parseDate(acceptedBeforeDateString);
        return parsedBeforeDate != null && parsedDate.isBefore(parsedBeforeDate);
    }

    private boolean isDateAfterValid(LocalDateTime parsedDate, String acceptedAfterDateString) {
        LocalDateTime parsedAfterDate = DateUtils.parseDate(acceptedAfterDateString);
        return parsedAfterDate != null && parsedDate.isAfter(parsedAfterDate);
    }

}
