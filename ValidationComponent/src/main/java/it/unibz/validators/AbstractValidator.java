package it.unibz.validators;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    protected AbstractValidator(final JsonNode validationRules){
        this.validationRules = validationRules;
        this.objectMapper = new ObjectMapper();
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

                // CAll some hook method that is abstract and then implement specific object type in each validator
                // Perform validation based on the field name and value
                applySingleRule(inputKey, inputValue, ruleFieldName, fieldFieldValue);
            }

        } else if (ruleValue.isArray()) {
            for (JsonNode innerNode : ruleValue) {
                parseJsonObject(inputKey, inputValue, innerNode);
            }
        }
    }

    /*
     * Data to validate comes as a JSON
     */
    public abstract ObjectNode validate(String key, JsonNode inputValue);
    protected abstract void applySingleRule(String key, T inputValue, String ruleName, JsonNode ruleValue);
    protected abstract void checkForViolation(boolean valid, String violationMessage);
    public abstract String getValidatorKey();
}
