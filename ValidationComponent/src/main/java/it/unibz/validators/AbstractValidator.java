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

    public boolean keyMatch(final JsonNode node, final String key) {
        return RegexUtils.regexMatch(node.get(KEY_FILTER_VALUE).textValue(), key);
    }

    /*
     * Here is assumed that inputValue is of primitive type [string, number, boolean, date]
     */
    public void parseJsonObject(String inputKey, T inputValue, JsonNode ruleValue) {
        if (ruleValue == null || ruleValue.isEmpty()) {
            return;
        }

        if (ruleValue.isObject()) {
            if (ruleValue.has("key_match")
                    && !keyMatch(ruleValue, inputKey)) {
                return;
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

    protected void checkForViolation(boolean ruleSatisfied, String violationMessage, Object... messageParameters) {
        if (!ruleSatisfied) {
            violationMessages.add(String.format(violationMessage, messageParameters));
        }
    }

    protected boolean isValueMatch(String inputValue, String constraintStringValue) {
        return RegexUtils.regexMatch(constraintStringValue, inputValue);
    }

    /*
     * Data to validate comes as a JSON
     */
    public abstract ObjectNode validate(String key, JsonNode inputValue);
    protected abstract void applySingleRule(String key, T inputValue, String ruleName, JsonNode ruleValue);
    public abstract String getValidatorKey();
}
