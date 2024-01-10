package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.constants.ViolationMessage;
import it.unibz.validators.AbstractValidator;

/**
 * Validator for boolean values.
 * <p>
 * Extends {@code AbstractValidator<Boolean>} and provides specific validation
 * logic for boolean values.
 */
public class BooleanValidator extends AbstractValidator<Boolean> {

    private static final String BOOLEAN_VALIDATOR_KEY = "boolean";

    private final ObjectNode booleanViolations;

    public BooleanValidator(JsonNode validationRules) {
        super(validationRules);

        this.booleanViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(final String inputKey, JsonNode inputValue) {
        if (JsonNodeType.BOOLEAN != inputValue.getNodeType()) {
            return null;
        }

        boolean booleanValue = inputValue.booleanValue();
        applyValidationRule(inputKey, booleanValue, validationRules);

        if (violationMessages.isEmpty()) {
            return objectMapper.createObjectNode();
        }

        booleanViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return booleanViolations;
    }

    @Override
    public void applySpecificValidationRule(String inputKey, Boolean inputValue, String ruleName, JsonNode ruleValue) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */
        boolean constrainBooleanValue = ruleValue.booleanValue();

        if (ruleName.equals("expected")) {
            checkForViolation(isValueAsExpected(inputValue, constrainBooleanValue),
                    ViolationMessage.RULE_EXPECTED_VIOLATION,
                    inputKey, inputValue, constrainBooleanValue);
        } else {
            throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    @Override
    public String getValidatorKey() {
        return BOOLEAN_VALIDATOR_KEY;
    }

    /**
     * Checks if a boolean value matches the expected boolean value.
     * <p>
     * This method compares the provided boolean value with the expected boolean value
     * to determine if they are equal.
     *
     * @param inputValue            The boolean value to be checked.
     * @param constrainBooleanValue The expected boolean value for comparison.
     * @return {@code true} if the boolean value matches the expected value, {@code false} otherwise.
     */
    private boolean isValueAsExpected(boolean inputValue, boolean constrainBooleanValue) {
        return inputValue == constrainBooleanValue;
    }
}
