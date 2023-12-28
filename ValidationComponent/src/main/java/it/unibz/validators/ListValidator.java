package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.List;
import java.util.Map;

public class ListValidator extends AbstractValidator<Object>{
    protected ListValidator(Map<String, List<String>> violations) {
        super(violations);
    }

    @Override
    public boolean validate(String key, JsonNode inputValue, JsonNode singleTypeValidationRules) {
        if (JsonNodeType.ARRAY != inputValue.getNodeType()) {
            return false;
        }

        parseRules(singleTypeValidationRules, key, inputValue);
        return false;
    }

    @Override
    protected void applySingleRule(Object inputValue, String ruleName, JsonNode ruleValue) {

    }


    @Override
    protected void resolveViolation(boolean valid, String violationMessage) {

    }

    @Override
    public String getValidatorKey() {
        return null;
    }
}
