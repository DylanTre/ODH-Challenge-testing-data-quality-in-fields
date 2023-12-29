package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;

public class NumberValidator extends AbstractValidator<Number> {

    private static final String NUMBER_VALIDATOR_KEY = "number";

    private static final String RULE_MORE_THAN_VIOLATION = "Number %s is not more than %s";
    private static final String RULE_LESS_THAN_VIOLATION = "Number %s is not less than %s";
    private static final String RULE_EQUAL_VIOLATION = "Number %s is not equal to %s";
    private static final String RULE_EVEN_VIOLATION = "Number %s is not even";
    private static final String RULE_ODD_VIOLATION = "Number %s is not odd";

    private static final double TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE = 1e-10;

    private final ArrayNode violationMessages;
    private final ObjectNode numberViolations;

    public NumberValidator(JsonNode validationRules) {
        super(validationRules);

        this.violationMessages = getObjectMapper().createArrayNode();
        this.numberViolations = getObjectMapper().createObjectNode();
    }


    @Override
    public ObjectNode validate(final String key, JsonNode inputValue) {
        if (JsonNodeType.NUMBER != inputValue.getNodeType()) {
            return null;
        }

        Number numericValue = inputValue.numberValue();
        parseJsonObject(key, numericValue, validationRules);

        numberViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return numberViolations;
    }

    @Override
    public void applySingleRule(String key, Number inputValue, String ruleName, JsonNode ruleValue) {
        boolean valid;
        Number constrainNumberValue = ruleValue.numberValue();

        switch (ruleName) {
            case "key_match" -> {}
            case "more_than" -> {
                valid = isMoreThan(inputValue, constrainNumberValue);
                checkForViolation(valid, String.format(RULE_MORE_THAN_VIOLATION, inputValue, constrainNumberValue));
            }
            case "less_than" -> {
                valid = !isMoreThan(inputValue, constrainNumberValue);
                checkForViolation(valid, String.format(RULE_LESS_THAN_VIOLATION, inputValue, constrainNumberValue));
            }
            case "equal" -> {
                valid = isEqual(inputValue, constrainNumberValue);
                checkForViolation(valid, String.format(RULE_EQUAL_VIOLATION, inputValue, constrainNumberValue));
            }
            case "even" -> {
                valid = isEven(inputValue);
                checkForViolation(valid, String.format(RULE_EVEN_VIOLATION, inputValue));
            }
            case "odd" -> {
                valid = !isEven(inputValue);
                checkForViolation(valid, String.format(RULE_ODD_VIOLATION, inputValue));
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
        return NUMBER_VALIDATOR_KEY;
    }


    public <T extends Number> boolean isMoreThan(T number, Number numberToCompare) {
        return number.doubleValue() - numberToCompare.doubleValue() > 0;
    }

    public <T extends Number> boolean isEqual(T number, Number numberToCompare) {
        return Math.abs(number.doubleValue() - numberToCompare.doubleValue()) < TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE;
    }

    /*
     * This method serves both even and odd number cases
     */
    public  boolean isEven(Number number) {
        // FIXME how to deal with double throw immediate exception or just return false?
        if (number instanceof Double) {
            return false;
        }
        return number != null && number.longValue() % 2 == 0;
    }

    public boolean isNaNOrInfinite(Double number) {
        return number != null && (Double.isNaN(number) || Double.isInfinite(number));
    }
}
