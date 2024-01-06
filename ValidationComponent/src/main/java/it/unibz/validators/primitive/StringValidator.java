package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.constants.ViolationMessage;
import it.unibz.utils.DateUtils;
import it.unibz.validators.AbstractValidator;

/**
 * Validator for string values.
 * <p>
 * Extends {@code AbstractValidator<String>} and provides specific validation
 * logic for string values.
 */
public class StringValidator extends AbstractValidator<String> {

    private static final String STRING_VALIDATOR_KEY = "string";

    private final ObjectNode stringViolations;

    public StringValidator(JsonNode validationRules) {
        super(validationRules);

        this.stringViolations = getObjectMapper().createObjectNode();
    }


    @Override
    public ObjectNode validate(final String key, JsonNode inputValue) {
        if (JsonNodeType.STRING != inputValue.getNodeType()) {
            return null;
        }

        String stringValue = inputValue.textValue();

        /*
         * If the input is parsed as date, string validator must not validate it
         */
        if (DateUtils.parseDate(stringValue) != null) {
            return null;
        }

        applyValidationRule(key, stringValue, validationRules);

        stringViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return stringViolations;
    }

    @Override
    public void applySpecificValidationRule(String key, String inputValue, String ruleName, JsonNode ruleValue) {
        String constraintStringValue = null;

        /*
         * toString() for numbers and textValue() for strings
         * because textValue() returns null for Numbers
         * enum rule can contain list of strings to match
         */
        if (JsonNodeType.STRING == ruleValue.getNodeType()) {
            constraintStringValue = ruleValue.textValue();
        } else if (JsonNodeType.NUMBER == ruleValue.getNodeType()) {
            constraintStringValue = ruleValue.toString();
        }

        switch (ruleName) {
            case "value_match" -> checkForViolation(isValueMatch(inputValue, constraintStringValue),
                    ViolationMessage.RULE_VALUE_MATCH_VIOLATION,
                    inputValue, constraintStringValue);

            case "enum" -> checkForViolation(isOneOf(inputValue, ruleValue),
                    ViolationMessage.RULE_ENUM_MATCH_VIOLATION,
                    inputValue, constraintStringValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    @Override
    public String getValidatorKey() {
        return STRING_VALIDATOR_KEY;
    }

    /**
     * Checks if the provided input value is one of the values specified in the "enum" property of the rule.
     * <p>
     * This method iterates through the elements of the "enum" property in the ruleValue JsonNode and compares
     * each element with the inputValue. If a match is found, the method returns true, otherwise false.
     *
     * @param inputValue The input value to be checked against the "enum" values.
     * @param ruleValue  The JsonNode representing the validation rule containing the "enum" property.
     * @return {@code true} if the inputValue is one of the values specified in the "enum" property, {@code false} otherwise.
     */
    private boolean isOneOf(String inputValue, JsonNode ruleValue) {
        if (JsonNodeType.ARRAY != ruleValue.getNodeType()) {
            return false;
        }

        for (JsonNode enumValue : ruleValue.path("enum")) {
            if (inputValue.equals(enumValue.asText())) {
                return true;
            }
        }
        return false;
    }
}