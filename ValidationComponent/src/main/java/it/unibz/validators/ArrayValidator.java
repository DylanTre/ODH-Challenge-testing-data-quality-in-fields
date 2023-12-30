package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.configuration.ConfigParser;
import it.unibz.enums.ValidatorType;
import it.unibz.violation.ViolationMessage;

import java.util.Map;

public class ArrayValidator extends AbstractValidator<JsonNode> {

    private static final String ARRAY_VALIDATOR_KEY = "array";

    private final Map<String, AbstractValidator> validators;
    private final ObjectNode arrayViolations;

    public ArrayValidator(JsonNode validationRules) {
        super(validationRules);

        ConfigParser config = new ConfigParser();
        this.validators = config.getGenericValidators(validationRules);

        this.arrayViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(String key, JsonNode inputValue) {
        ObjectNode listValuesViolations = objectMapper.createObjectNode();
        if (JsonNodeType.ARRAY != inputValue.getNodeType()) {
            return null;
        }

        if (keyMatch(validationRules, key)) {
            listValuesViolations = parseInputArrayValues(inputValue);
        }

        arrayViolations.putIfAbsent(getValidatorKey(), listValuesViolations);
        return arrayViolations;
    }

    @Override
    protected void applySingleRule(String key, JsonNode inputValue, String ruleName, JsonNode ruleValue) {
        switch (ruleName) {
            case "key_match" -> {}
            case "contains" -> checkForViolation(isListContainsValues(inputValue, ruleValue),
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

    private boolean isListContainsValues(JsonNode inputList, JsonNode ruleValue) {
        for (JsonNode targetValue : ruleValue) {
            boolean containsValue = false;
            for (JsonNode node : inputList) {
                if (node.toString().equals(targetValue.toString())) {
                    containsValue = true;
                    break;
                }
            }
            if (!containsValue) {
                return false; // At least one value is not present
            }
        }
        return true;
    }

    private boolean isCorrectType(JsonNode inputList, JsonNode ruleValue) {
        // FIXME don't even validate if rule has more validators than expected type

        if (inputList.isEmpty()) {
            // If the array has 0 or 1 element, they are considered of the same type
            return true;
        }

        for (int i = 0; i < inputList.size(); i++) {
            if (inputList.get(i).getNodeType() != ruleValue.getNodeType()) {
                return false;
            }
        }

        return true;
    }


    private ObjectNode parseInputArrayValues(JsonNode inputArray) {
        ObjectNode violations = getObjectMapper().createObjectNode();
        if (inputArray.isArray()) {
            for (JsonNode arrayValue : inputArray) {
                AbstractValidator validator = validators.get(ValidatorType.of(arrayValue.getNodeType()));

                // Validator defined for list object
                if (validator != null) {
                    // FIXME at the moment cannot think of a better way to produce violation output to the user
                    violations.putIfAbsent(validator.getValidatorKey(), validator.validate(null, arrayValue));
                }
            }
        }

        return violations;
    }
}
