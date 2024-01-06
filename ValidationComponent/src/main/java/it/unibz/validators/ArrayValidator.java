package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.DataValidator;
import it.unibz.configuration.ConfigParser;
import it.unibz.constants.ViolationMessage;

/**
 * Validator for array values.
 * <p>
 * Can contain other primitive validators inside
 * <p>
 * Extends {@code AbstractValidator<JsonNode>} and provides specific validation
 * logic for array values.
 */
public class ArrayValidator extends AbstractValidator<JsonNode> {

    private static final String ARRAY_VALIDATOR_KEY = "array";

    private final DataValidator dataValidator;
    private final ObjectNode arrayViolations;

    public ArrayValidator(JsonNode validationRules) {
        super(validationRules);

        ConfigParser config = new ConfigParser();
        this.dataValidator = new DataValidator(config.getGenericValidators(validationRules));
        this.arrayViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(String key, JsonNode inputValue) {
        ObjectNode arrayValuesViolations = objectMapper.createObjectNode();
        if (JsonNodeType.ARRAY != inputValue.getNodeType()) {
            return null;
        }

        if (keyMatch(validationRules, key)) {
            arrayValuesViolations = validateArrayValues(inputValue);
        }

        arrayViolations.putIfAbsent(getValidatorKey(), arrayValuesViolations);
        return arrayViolations;
    }

    @Override
    protected void applySpecificValidationRule(String key, JsonNode inputValue, String ruleName, JsonNode ruleValue) {
        switch (ruleName) {
            case "contains" -> checkForViolation(arrayContainsValues(inputValue, ruleValue),
                    ViolationMessage.RULE_ARRAY_CONTAINS_VIOLATION,
                    key, ruleValue);

            case "type" -> checkForViolation(isCorrectType(inputValue, ruleValue),
                    ViolationMessage.RULE_ARRAY_CORRECT_TYPE_VIOLATION,
                    key, ruleValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    @Override
    public String getValidatorKey() {
        return ARRAY_VALIDATOR_KEY;
    }

    /**
     * Checks if the input array contains all values specified in the ruleValue array.
     * <p>
     * This method iterates through the values in the ruleValue array and checks if each
     * value is present in the inputArray. If any value is missing, the method returns false.
     *
     * @param inputArray  The JsonNode representing the input array.
     * @param ruleValue  The JsonNode representing the array of values to check against.
     * @return {@code true} if the input array contains all values specified in the ruleValue array, otherwise {@code false}.
     */
    private boolean arrayContainsValues(JsonNode inputArray, JsonNode ruleValue) {
        for (JsonNode targetValue : ruleValue) {
            boolean containsValue = false;
            for (JsonNode node : inputArray) {
                if (node.toString().equals(targetValue.toString())) {
                    containsValue = true;
                    break;
                }
            }
            if (!containsValue) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks if the input array has elements of the correct type as specified in the ruleValue.
     * <p>
     * This method compares the node type of each element in the input array with the node type
     * specified in the ruleValue. If any element has a different node type, the method returns false.
     *
     * @param inputArray  The JsonNode representing the input array.
     * @param ruleValue  The JsonNode representing the expected node type for elements in the input array.
     * @return {@code true} if the input array has elements of the correct type, {@code false} otherwise.
     */
    private boolean isCorrectType(JsonNode inputArray, JsonNode ruleValue) {
        if (inputArray.isEmpty()) {
            /*
             * If the array has 0 elements it is considered of the same type
             */
            return true;
        }

        for (int i = 0; i < inputArray.size(); i++) {
            if (inputArray.get(i).getNodeType() != ruleValue.getNodeType()) {
                return false;
            }
        }

        return true;
    }


    /**
     * Validates each value in the input array using registered validators.
     * <p>
     * This method applies registered validators to each value in the inputArray
     * and collects the validation results in an ObjectNode, which is then returned.
     *
     * @param inputArray The JsonNode representing the input array to be validated.
     * @return An ObjectNode containing violation details, if any violations are found during validation.
     */
    private ObjectNode validateArrayValues(JsonNode inputArray) {
        ObjectNode violations = objectMapper.createObjectNode();

        if (inputArray.isArray()) {
            for (JsonNode arrayValue : inputArray) {
                dataValidator.applyValidators(null, arrayValue, violations);
            }
        }

        return violations;
    }
}
