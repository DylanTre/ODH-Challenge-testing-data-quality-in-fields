package it.unibz.validators;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.constants.ViolationMessage;
import it.unibz.utils.RegexUtils;
import lombok.Getter;

import java.util.Iterator;

/**
 * Base class for implementing validators.
 * <p>
 * This abstract class provides a foundation for creating custom validators by defining common
 * methods and structures. It is parameterized with type {@code T} representing the type of
 * values to be validated.
 *
 * @param <T> The type of values to be validated.
 */
public abstract class AbstractValidator<T> {

    protected final String KEY_FILTER_VALUE = "key_match";

    @Getter
    protected final ObjectMapper objectMapper;
    @Getter
    protected final JsonNode validationRules;
    @Getter
    protected final ArrayNode violationMessages;

    protected AbstractValidator(final JsonNode validationRules){
        this.objectMapper = new ObjectMapper();
        this.validationRules = validationRules;
        this.violationMessages = getObjectMapper().createArrayNode();
    }

    /**
     * Checks if the value associated with the specified key in the given JSON node
     * matches the provided regular expression pattern.
     *
     * @param node The JSON node containing key-value pairs.
     * @param key  The key whose associated value is to be checked.
     * @return {@code true} if the value associated with the specified key matches
     *         the provided regular expression pattern; {@code false} otherwise.
     */
    protected boolean keyMatch(final JsonNode node, final String key) {
        return RegexUtils.regexMatch(node.get(KEY_FILTER_VALUE).textValue(), key);
    }

    /**
     * Recursively parses a JSON rule object, applying validation rules based on the
     * field names and values within the object.
     * Assumes that inputValue is of primitive type [string, number, boolean, date]
     *
     * @param inputKey    The key associated with the input value.
     * @param inputValue  The input value to be validated.
     * @param ruleValue   The JSON node representing the validation rules.
     */
    protected void applyValidationRule(String inputKey, T inputValue, JsonNode ruleValue) {
        if (ruleValue == null || ruleValue.isEmpty()) {
            return;
        }

        if (ruleValue.isObject() && ruleValue.has("key_match")) {
            if (!keyMatch(ruleValue, inputKey)) {
                return;
            }
        }

        if (ruleValue.isObject()) {
            validateObjectFields(inputKey, inputValue, ruleValue);
        } else if (ruleValue.isArray()) {
            validateArrayElements(inputKey, inputValue, ruleValue);
        }
    }

    /**
     * Adds a violation message to the list if the specified rule is not satisfied.
     *
     * @param ruleSatisfied       Indicates whether the rule is satisfied.
     * @param violationMessage    The violation message format string.
     * @param messageParameters   The parameters to be formatted into the violation message.
     */
    protected void checkForViolation(boolean ruleSatisfied, String violationMessage, Object... messageParameters) {
        if (!ruleSatisfied) {
            violationMessages.add(String.format(violationMessage, messageParameters));
        }
    }

    /**
     * Validates fields of an object based on the provided validation rules.
     * <p>
     * Iterates through the fields of the object and applies validation rules to each field.
     *
     * @param inputKey          The key of the input.
     * @param inputValue        The value of the input.
     * @param ruleValue     The validation rules for the object.
     */
    private void validateObjectFields(String inputKey, T inputValue, JsonNode ruleValue) {
        ObjectNode objectNode = (ObjectNode) ruleValue;
        Iterator<String> fieldNames = objectNode.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldValue = objectNode.get(fieldName);

            if (fieldValue.isObject()) {
                applyValidationRule(inputKey, inputValue, fieldValue);
            } else if (fieldValue.isArray()) {
                validateArrayElements(inputKey, inputValue, fieldValue);
            }

            applyGlobalValidationRule(inputKey, inputValue, fieldName, fieldValue);
        }
    }

    /**
     * Validates elements of an array based on the provided validation rules.
     * <p>
     * Iterates through the elements of the array and applies validation rules to each element.
     *
     * @param validationKey   The key associated with the validation.
     * @param validationValue The value to be validated.
     * @param arrayNode       The validation rules for the array.
     */
    private void validateArrayElements(String validationKey, T validationValue, JsonNode arrayNode) {
        for (JsonNode innerNode : arrayNode) {
            applyValidationRule(validationKey, validationValue, innerNode);
        }
    }


    /**
     * Checks if the input value matches a given regular expression pattern.
     *
     * @param inputValue             The input value to be checked.
     * @param constraintRegex        The regular expression pattern to match against.
     * @return {@code true} if the input value matches the pattern; {@code false} otherwise.
     */
    protected boolean isValueMatch(String inputValue, String constraintRegex) {
        return RegexUtils.regexMatch(constraintRegex, inputValue);
    }

    /**
     * Applies a global validation rule to the provided input value based on the ruleName.
     * <p>
     * This method handles global validation rules recognized by every validator.
     * If rule is not global, it is specific and is applied in a child
     *
     * @param inputKey        The inputKey associated with the input value being validated.
     * @param inputValue      The input value to be validated.
     * @param ruleName        The name of the global validation rule.
     * @param ruleValue   The JsonNode representing the value associated with the validation rule.
     */
    protected void applyGlobalValidationRule(String inputKey, T inputValue, String ruleName, JsonNode ruleValue) {
        switch (ruleName) {
            case "key_match" -> {}

            /*
             * Example for
             * There should be possible to associate naming conventions
             * (using regular expressions) to validation rules.
             */
            case "name_pattern" -> checkForViolation(isValueMatch(inputKey, ruleValue.textValue()),
                    ViolationMessage.RULE_NAME_PATTERN_VIOLATION,
                    inputKey, inputValue, ruleValue.textValue());

            default -> applySpecificValidationRule(inputKey, inputValue, ruleName, ruleValue);
        }
    }

    /**
     * Validates the provided JSON input value for a specified inputKey and returns
     * an ObjectNode containing validation results.
     *
     * @param inputKey   The inputKey associated with the input value.
     * @param inputValue The JSON node representing the input value to be validated.
     * @return An ObjectNode containing validation results, or null if validation is not applicable.
     */
    public abstract ObjectNode validate(String inputKey, JsonNode inputValue);

    /**
     * Applies a specific validation rule to the input value based on the rule name and value.
     * This method is called during the validation process.
     *
     * @param inputKey        The inputKey associated with the input value.
     * @param inputValue The input value to be validated.
     * @param ruleName   The name of the validation rule.
     * @param ruleValue  The JSON node representing the value of the validation rule.
     */
    protected abstract void applySpecificValidationRule(String inputKey, T inputValue, String ruleName, JsonNode ruleValue);

    /**
     * Gets the key associated with the validator. This key is used to identify
     * the validator within a validation framework.
     *
     * @return The key associated with the validator.
     */
    public abstract String getValidatorKey();
}
