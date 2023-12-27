package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NumberValidator extends AbstractValidator {

    private static final String NUMBER_VIOLATION_KEY = "number";

    private static final String RULE_MORE_THAN_VIOLATION = "Number %s is not more than %s";
    private static final String RULE_LESS_THAN_VIOLATION = "Number %s is not less than %s";
    private static final String RULE_EQUAL_VIOLATION = "Number %s is not equal to %s";
    private static final String RULE_EVEN_VIOLATION = "Number %s is not even";
    private static final String RULE_ODD_VIOLATION = "Number %s is not odd";

    private static final double TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE = 1e-10;

    private final List<String> numberViolations;

    public NumberValidator(Map<String, List<String>> violations) {
        super(violations);

        this.numberViolations = new ArrayList<>();
    }


    @Override
    public boolean validate(final String key, Object inputValue, JsonNode numberValidationRules) {
        Number numericValue = (Number) inputValue;

        parseJsonNode(numberValidationRules, key, numericValue);
        return false;
    }

    @Override
    public void resolveViolation(boolean valid, String violationMessage) {
        if (!valid) {
            numberViolations.add(violationMessage);
        }
    }

    public void applySingleRule(Number fieldValue, String constraintKey, JsonNode constraintValue) {
        boolean valid;
        Number constrainNumberValue = constraintValue.numberValue();

        switch (constraintKey) {
            case "key_match" -> {}
            case "more_than" -> {
                valid = isMoreThan(fieldValue, constrainNumberValue);
                resolveViolation(valid, String.format(RULE_MORE_THAN_VIOLATION, fieldValue, constrainNumberValue));
            }
            case "less_than" -> {
                valid = !isMoreThan(fieldValue, constrainNumberValue);
                resolveViolation(valid, String.format(RULE_LESS_THAN_VIOLATION, fieldValue, constrainNumberValue));
            }
            case "equal" -> {
                valid = isEqual(fieldValue, constrainNumberValue);
                resolveViolation(valid, String.format(RULE_EQUAL_VIOLATION, fieldValue, constrainNumberValue));
            }
            case "even" -> {
                valid = isEven(fieldValue);
                resolveViolation(valid, String.format(RULE_EVEN_VIOLATION, fieldValue));
            }
            case "odd" -> {
                valid = !isEven(fieldValue);
                resolveViolation(valid, String.format(RULE_ODD_VIOLATION, fieldValue));
            }
            default -> throw new IllegalArgumentException("Rule " + constraintKey + "unrecognized");
        }

        /*
         * FIXME
         *  idea would be instead of just number=[Number 253 is not even]
         *  have field1=[field1_violations], field2=[field2_violations]
         */
        violations.put(NUMBER_VIOLATION_KEY, numberViolations);
    }


    public boolean isMoreThan(Number number, Number numberToCompare) {
        return number.doubleValue() - numberToCompare.doubleValue() > 0;
    }

    public boolean isEqual(Number number, Number numberToCompare) {
        return Math.abs(number.doubleValue() - numberToCompare.doubleValue()) < TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE;
    }

    /*
     * This method serves both even and odd number cases
     */
    public boolean isEven(Number number) {
        // FIXME how to deal with double throw immediate exception or just return false?
        if (number instanceof Double) {
            return false;
        }
        return number != null && number.longValue() % 2 == 0;
    }

    public boolean isNaNOrInfinite(Double number) {
        return number != null && (Double.isNaN(number) || Double.isInfinite(number));
    }

    public void parseJsonNode(JsonNode node, String inputKey, Number numericInputValue) {
        if (node == null || node.isEmpty()) {
            return;
        }  else if (node.isObject()) {
            if (!keyMatch(node, inputKey)) {
                return;
            }

            ObjectNode objectNode = (ObjectNode) node;
            Iterator<String> fieldNames = objectNode.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = objectNode.get(fieldName);

                // Perform validation based on the field name and value
                applySingleRule(numericInputValue, fieldName, fieldValue);
            }

        } else if (node.isArray()) {
            for (JsonNode innerNode : node) {
                parseJsonNode(innerNode, inputKey, numericInputValue);
            }
        }
    }
}
