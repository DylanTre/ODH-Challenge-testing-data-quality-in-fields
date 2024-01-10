package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.constants.ViolationMessage;
import it.unibz.validators.AbstractValidator;

/**
 * Validator for number values.
 * <p>
 * Extends {@code AbstractValidator<Number>} and provides specific validation
 * logic for number values.
 */
public class NumberValidator extends AbstractValidator<Number> {

    private static final String NUMBER_VALIDATOR_KEY = "number";
    private static final double TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE = 1e-10;
    private final ObjectNode numberViolations;

    public NumberValidator(JsonNode validationRules) {
        super(validationRules);

        this.numberViolations = getObjectMapper().createObjectNode();
    }


    @Override
    public ObjectNode validate(final String inputKey, JsonNode inputValue) {
        if (JsonNodeType.NUMBER != inputValue.getNodeType()) {
            return null;
        }

        Number numericValue = inputValue.numberValue();
        applyValidationRule(inputKey, numericValue, validationRules);

        if (violationMessages.isEmpty()) {
            return objectMapper.createObjectNode();
        }

        numberViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return numberViolations;
    }

    @Override
    public void applySpecificValidationRule(String inputKey, Number inputValue, String ruleName, JsonNode ruleValue) {
        Number constrainNumberValue = null;
        /*
         * enum rule can contain list of strings to match
         */
        if (ruleValue.isNumber()) {
            constrainNumberValue = ruleValue.numberValue();
        }

        switch (ruleName) {
            case "min" -> checkForViolation(isMoreThan(inputValue, constrainNumberValue),
                    ViolationMessage.RULE_MORE_THAN_VIOLATION,
                    inputKey, inputValue, constrainNumberValue);

            case "max" -> checkForViolation(!isMoreThan(inputValue, constrainNumberValue),
                    ViolationMessage.RULE_LESS_THAN_VIOLATION,
                    inputKey, inputValue, constrainNumberValue);

            case "equal" -> checkForViolation(isEqual(inputValue, constrainNumberValue),
                    ViolationMessage.RULE_EQUAL_VIOLATION,
                    inputKey, inputValue, constrainNumberValue);

            case "even" -> checkForViolation(isEven(inputValue),
                    ViolationMessage.RULE_EVEN_VIOLATION,
                    inputKey, inputValue);

            case "odd" -> checkForViolation(isOdd(inputValue),
                    ViolationMessage.RULE_ODD_VIOLATION,
                    inputKey, inputValue);

            case "enum" -> checkForViolation(isOneOf(inputValue, ruleValue),
                    ViolationMessage.RULE_ENUM_MATCH_VIOLATION,
                    inputKey, inputValue, ruleValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    @Override
    public String getValidatorKey() {
        return NUMBER_VALIDATOR_KEY;
    }

    /**
     * Checks if the given number is greater than another number.
     * <p>
     * This method compares two numbers using their double values.
     * If the provided comparison number is null or the input number is NaN or infinite, the result is false.
     *
     * @param number           The number to be compared.
     * @param numberToCompare  The number to compare against.
     * @param <T>              The type of the numbers.
     * @return {@code true} if the provided number is greater than the comparison number, {@code false} otherwise.
     */
    private <T extends Number> boolean isMoreThan(T number, Number numberToCompare) {
        if (numberToCompare == null || isNaNOrInfinite(number.doubleValue())) {
            return false;
        }
        return number.doubleValue() > numberToCompare.doubleValue();
    }

    /**
     * Checks if the given number is approximately equal to another number within a tolerated floating-point difference.
     * <p>
     * This method compares two numbers using their double values.
     * If the provided comparison number is null or the input number is NaN or infinite, the result is false.
     *
     * @param number           The number to be compared.
     * @param numberToCompare  The number to compare against.
     * @param <T>              The type of the numbers.
     * @return {@code true} if the absolute difference between the provided number and the comparison number is within
     * the tolerated floating-point difference, {@code false} otherwise.
     */
    private <T extends Number> boolean isEqual(T number, Number numberToCompare) {
        if (numberToCompare == null || isNaNOrInfinite(number.doubleValue())) {
            return false;
        }
        return Math.abs(number.doubleValue() - numberToCompare.doubleValue()) < TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE;
    }

    /**
     * Checks if the given number is even.
     * <p>
     * This method returns false if the number is an instance of Double.
     *
     * @param number The number to be checked.
     * @return {@code true} if the provided number is not an instance of Double and is even, {@code false} otherwise.
     */
    private boolean isEven(Number number) {
        return !(number instanceof Double) && number != null && number.longValue() % 2 == 0;
    }

    /**
     * Checks if the given number is odd.
     * <p>
     * This method returns false if the number is an instance of Double.
     *
     * @param number The number to be checked.
     * @return {@code true} if the provided number is not an instance of Double and is odd, {@code false} otherwise.
     */
    private boolean isOdd(Number number) {
        return !(number instanceof Double) && number != null && number.longValue() % 2 == 1;
    }

    /**
     * Checks if the provided double value is NaN or infinite.
     *
     * @param number The double value to check.
     * @return {@code true} if the value is not null and is NaN or infinite, {@code false} otherwise.
     */
    private boolean isNaNOrInfinite(Double number) {
        return number != null && (Double.isNaN(number) || Double.isInfinite(number));
    }

    /**
     * Checks if the given number is one of the numbers specified in the "enum" property of the rule.
     * <p>
     * This method iterates through the elements of the "enum" property in the ruleValue JsonNode and compares
     * each element with the input number. If a match is found, the method returns true, otherwise false.
     *
     * @param inputValue The input number to be checked against the "enum" values.
     * @param ruleValue  The JsonNode representing the validation rule containing the "enum" property.
     * @return {@code true} if the input number is one of the values specified in the "enum" property, {@code false} otherwise.
     */
    private boolean isOneOf(Number inputValue, JsonNode ruleValue) {
        if (JsonNodeType.ARRAY != ruleValue.getNodeType()) {
            return false;
        }

        for (JsonNode enumValue : ruleValue.path("enum")) {
            if (inputValue.equals(enumValue.numberValue())) {
                return true;
            }
        }
        return false;
    }


}
