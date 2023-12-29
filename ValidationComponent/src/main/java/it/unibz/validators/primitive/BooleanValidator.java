package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;

public class BooleanValidator extends AbstractValidator<Boolean> {

    private static final String BOOLEAN_VALIDATOR_KEY = "boolean";

    private static final String RULE_EXPECTED_VIOLATION = "%s was expected to be %s";

    private final ArrayNode violationMessages;
    private final ObjectNode booleanViolations;

    public BooleanValidator(JsonNode validationRules) {
        super(validationRules);

        this.violationMessages = getObjectMapper().createArrayNode();
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
        boolean valid;
        boolean constrainBooleanValue = ruleValue.booleanValue();

        switch (ruleName) {
            case "key_match" -> {}
            case "expected" -> {
                valid = inputValue == constrainBooleanValue;
                checkForViolation(valid, String.format(RULE_EXPECTED_VIOLATION, key, constrainBooleanValue));
            }
            default -> throw new IllegalArgumentException("Rule " + ruleName + "unrecognized");
        }
    }

    @Override
    public void checkForViolation(boolean valid, String violationMessage) {
        if (!valid) {
            violationMessages.add(violationMessage);
        }
    }

    @Override
    public String getValidatorKey() {
        return BOOLEAN_VALIDATOR_KEY;
    }
}
