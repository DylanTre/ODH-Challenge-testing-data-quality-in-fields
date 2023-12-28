package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BooleanValidator extends AbstractValidator<Boolean> {

    private static final String BOOLEAN_VALIDATOR_KEY = "boolean";

    private static final String RULE_EXPECTED_VIOLATION = "Boolean %s was expected to be %s";

    private final List<String> booleanViolations;
    public BooleanValidator(final Map<String, List<String>> violations) {
        super(violations);
        this.booleanViolations = new ArrayList<>();
    }

    @Override
    public boolean validate(final String key, JsonNode inputValue, JsonNode booleanValidationRules) {
        if (JsonNodeType.BOOLEAN != inputValue.getNodeType()) {
            return false;
        }

        boolean booleanValue = inputValue.booleanValue();

        parseRules(booleanValidationRules, key, booleanValue);
        return false;
    }

    @Override
    public void applySingleRule(Boolean inputValue, String ruleName, JsonNode ruleValue) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */
        boolean valid;
        boolean constrainBooleanValue = ruleValue.booleanValue();

        switch (ruleName) {
            case "key_match" -> {}
            case "expected" -> {
                valid = inputValue == constrainBooleanValue;
                resolveViolation(valid, String.format(RULE_EXPECTED_VIOLATION, inputValue, constrainBooleanValue));
            }
            default -> throw new IllegalArgumentException("Rule " + ruleName + "unrecognized");
        }

        violations.put(BOOLEAN_VALIDATOR_KEY, booleanViolations);
    }

    @Override
    public void resolveViolation(boolean valid, String violationMessage) {
        if (!valid) {
            booleanViolations.add(BOOLEAN_VALIDATOR_KEY);
        }
    }

    @Override
    public String getValidatorKey() {
        return BOOLEAN_VALIDATOR_KEY;
    }
}
