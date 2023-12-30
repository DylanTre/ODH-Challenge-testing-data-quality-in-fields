package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;
import it.unibz.violation.ViolationMessage;

public class NumberValidator extends AbstractValidator<Number> {

    private static final String NUMBER_VALIDATOR_KEY = "number";
    private static final double TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE = 1e-10;
    private final ObjectNode numberViolations;

    public NumberValidator(JsonNode validationRules) {
        super(validationRules);

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
        Number constrainNumberValue = null;
        // enum rule can contain list of strings to match
        if (ruleValue.isNumber()) {
            constrainNumberValue = ruleValue.numberValue();
        }

        switch (ruleName) {
            case "key_match" -> {}

            case "min" -> checkForViolation(isMoreThan(inputValue, constrainNumberValue),
                    ViolationMessage.RULE_MORE_THAN_VIOLATION,
                    inputValue, constrainNumberValue);

            case "max" -> checkForViolation(!isMoreThan(inputValue, constrainNumberValue),
                    ViolationMessage.RULE_LESS_THAN_VIOLATION,
                    inputValue, constrainNumberValue);

            case "equal" -> checkForViolation(isEqual(inputValue, constrainNumberValue),
                    ViolationMessage.RULE_EQUAL_VIOLATION,
                    inputValue, constrainNumberValue);

            case "even" -> checkForViolation(isEven(inputValue),
                    ViolationMessage.RULE_EVEN_VIOLATION,
                    inputValue);

            case "odd" -> checkForViolation(!isEven(inputValue),
                    ViolationMessage.RULE_ODD_VIOLATION,
                    inputValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    @Override
    public String getValidatorKey() {
        return NUMBER_VALIDATOR_KEY;
    }

    public <T extends Number> boolean isMoreThan(T number, Number numberToCompare) {
        return number != null && number.doubleValue() - numberToCompare.doubleValue() > 0;
    }

    public <T extends Number> boolean isEqual(T number, Number numberToCompare) {
        return number != null
                && Math.abs(number.doubleValue() - numberToCompare.doubleValue()) < TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE;
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
