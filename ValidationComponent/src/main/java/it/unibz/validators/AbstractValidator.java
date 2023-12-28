package it.unibz.validators;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.utils.RegexUtils;
import lombok.Getter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractValidator<T> {

    protected final String KEY_FILTER_VALUE = "key_match";

    @Getter
    protected final Map<String, List<String>> violations;

    protected AbstractValidator(final Map<String, List<String>> violations){
        this.violations = violations;
    }

    /*
     * Data to validate comes as a JSON
     */
    public abstract boolean validate(String key, JsonNode inputValue, JsonNode singleTypeValidationRules);
    protected abstract void applySingleRule(T inputValue, String ruleName, JsonNode ruleValue);
    protected abstract void resolveViolation(boolean valid, String violationMessage);
    public abstract String getValidatorKey();

    public boolean keyMatch(final JsonNode node, final String key) {
        return RegexUtils.regexMatch(node.get(KEY_FILTER_VALUE).textValue(), key);
    }

    public void parseRules(JsonNode node, String inputKey, T inputValue) {
        if (node == null || node.isEmpty()) {
            return;
        }

        if (node.isObject()) {
            if (!keyMatch(node, inputKey)) {
                return;
            }

            ObjectNode objectNode = (ObjectNode) node;
            Iterator<String> fieldNames = objectNode.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = objectNode.get(fieldName);

                // Recursive validation for nested objects
                if (fieldValue.isObject()) {
                    parseRules(node, inputKey, inputValue);
                }

                // CAll some hook method that is abstract and then implement specific object type in each validator
                // Perform validation based on the field name and value
                applySingleRule(inputValue, fieldName, fieldValue);
            }

        } else if (node.isArray()) {
            for (JsonNode innerNode : node) {
                parseRules(innerNode, inputKey, inputValue);
            }
        }
    }

}
