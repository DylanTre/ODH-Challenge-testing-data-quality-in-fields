package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;
import it.unibz.violation.ViolationMessage;

public class BooleanValidator extends AbstractValidator<Boolean> {

    private static final String BOOLEAN_VALIDATOR_KEY = "boolean";

    private final ObjectNode booleanViolations;

    public BooleanValidator(JsonNode validationRules) {
        super(validationRules);

        this.booleanViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(final String key, JsonNode inputValue) {
        if (JsonNodeType.BOOLEAN != inputValue.getNodeType()) {
            return null;
        }

        boolean booleanValue = inputValue.booleanValue();

        parseJsonObject(key, booleanValue, validationRules);

        booleanViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return booleanViolations;
    }

    @Override
    public void applySingleRule(String key, Boolean inputValue, String ruleName, JsonNode ruleValue) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */

        boolean constrainBooleanValue = ruleValue.booleanValue();

        switch (ruleName) {
            case "key_match" -> {}

            case "name_pattern" -> checkForViolation(isValueMatch(key, ruleValue.textValue()),
                    ViolationMessage.RULE_NAME_PATTERN_VIOLATION,
                    key, ruleValue.textValue());

            case "expected" -> checkForViolation(isValueAsExpected(inputValue, constrainBooleanValue),
                    ViolationMessage.RULE_EXPECTED_VIOLATION,
                    key, constrainBooleanValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    private boolean isValueAsExpected(boolean inputValue, boolean constrainBooleanValue) {
        return inputValue == constrainBooleanValue;
    }

    @Override
    public String getValidatorKey() {
        return BOOLEAN_VALIDATOR_KEY;
    }
}
