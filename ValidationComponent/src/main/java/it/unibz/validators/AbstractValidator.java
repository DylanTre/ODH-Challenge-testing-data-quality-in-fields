package it.unibz.validators;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.utils.RegexUtils;
import lombok.Getter;

import java.util.Iterator;

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
     * Recursively parses a JSON object, applying validation rules based on the
     * field names and values within the object.
     * Assumed that inputValue is of primitive type [string, number, boolean, date]
     *
     * @param inputKey    The key associated with the input value.
     * @param inputValue  The input value to be validated.
     * @param ruleValue   The JSON node representing the validation rules.
     */
    protected void parseJsonObject(String inputKey, T inputValue, JsonNode ruleValue) {
        if (ruleValue == null || ruleValue.isEmpty()) {
            return;
        }

        if (ruleValue.isObject()) {
            if (ruleValue.has("key_match")) {
                if (!keyMatch(ruleValue, inputKey)) {
                    return;
                }
            }

            ObjectNode objectNode = (ObjectNode) ruleValue;
            Iterator<String> fieldNames = objectNode.fieldNames();

            while (fieldNames.hasNext()) {
                String ruleFieldName = fieldNames.next();
                JsonNode fieldFieldValue = objectNode.get(ruleFieldName);

                // Recursive validation for nested objects
                if (fieldFieldValue.isObject()) {
                    parseJsonObject(inputKey, inputValue, fieldFieldValue);
                } else if (fieldFieldValue.isArray()) {
                    for (JsonNode innerNode : fieldFieldValue) {
                        parseJsonObject(inputKey, inputValue, innerNode);
                    }
                }

                // Perform validation based on the field name and value
                applySingleRule(inputKey, inputValue, ruleFieldName, fieldFieldValue);
            }

        } else if (ruleValue.isArray()) {
            for (JsonNode innerNode : ruleValue) {
                parseJsonObject(inputKey, inputValue, innerNode);
            }
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
     * Checks if the input value matches a given regular expression pattern.
     *
     * @param inputValue             The input value to be checked.
     * @param constraintStringValue  The regular expression pattern to match against.
     * @return {@code true} if the input value matches the pattern; {@code false} otherwise.
     */
    protected boolean isValueMatch(String inputValue, String constraintStringValue) {
        return RegexUtils.regexMatch(constraintStringValue, inputValue);
    }

    /**
     * Validates the provided JSON input value for a specified key and returns
     * an ObjectNode containing validation results.
     *
     * @param key         The key associated with the input value.
     * @param inputValue  The JSON node representing the input value to be validated.
     * @return An ObjectNode containing validation results, or null if validation is not applicable.
     */
    public abstract ObjectNode validate(String key, JsonNode inputValue);

    /**
     * Applies a single validation rule to the input value based on the rule name and value.
     * This method is called during the validation process.
     *
     * @param key        The key associated with the input value.
     * @param inputValue The input value to be validated.
     * @param ruleName   The name of the validation rule.
     * @param ruleValue  The JSON node representing the value of the validation rule.
     */
    protected abstract void applySingleRule(String key, T inputValue, String ruleName, JsonNode ruleValue);

    /**
     * Gets the key associated with the validator. This key is used to identify
     * the validator within a validation framework.
     *
     * @return The key associated with the validator.
     */
    public abstract String getValidatorKey();
}
