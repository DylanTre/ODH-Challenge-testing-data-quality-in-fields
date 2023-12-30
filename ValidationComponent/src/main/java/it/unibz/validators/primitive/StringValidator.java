package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.utils.StringUtils;
import it.unibz.validators.AbstractValidator;
import it.unibz.violation.ViolationMessage;

import java.util.List;

public class StringValidator extends AbstractValidator<String> {

    private static final String STRING_VALIDATOR_KEY = "string";
    public static final List<JsonNodeType> ACCEPTED_TYPES = List.of(JsonNodeType.STRING, JsonNodeType.NUMBER);

    private final ObjectNode stringViolations;

    public StringValidator(JsonNode validationRules) {
        super(validationRules);

        this.stringViolations = getObjectMapper().createObjectNode();
    }


    @Override
    public ObjectNode validate(final String key, JsonNode inputValue) {
        if (JsonNodeType.STRING != inputValue.getNodeType()) {
            return null;
        }

        String stringValue = inputValue.textValue();

        // FIXME solve the changing pattern problem
        //  set DEFAULT pattern somehow
        if (StringUtils.isDateTime(stringValue, "yyyy-MM-dd HH:mm:ss")) {
            return null;
        }

        parseJsonObject(key, stringValue, validationRules);

        stringViolations.putIfAbsent(getValidatorKey(), violationMessages);
        return stringViolations;
    }

    @Override
    public void applySingleRule(String key, String inputValue, String ruleName, JsonNode ruleValue) {
        String constraintStringValue = null;
        // toString() instead of textValue() because textValue() returns null for Numbers
        // enum rule can contain list of strings to match
        if (ACCEPTED_TYPES.contains(ruleValue.getNodeType())) {
            constraintStringValue = ruleValue.toString();
        }

        switch (ruleName) {
            case "key_match" -> {}

            // Example There should be possible to associate naming conventions
            //(using regular expressions) to validation rules.
            case "name_pattern" -> checkForViolation(isValueMatch(key, constraintStringValue),
                        ViolationMessage.RULE_NAME_PATTERN_VIOLATION,
                        key, constraintStringValue);

            case "value_match" -> checkForViolation(isValueMatch(inputValue, constraintStringValue),
                    ViolationMessage.RULE_VALUE_MATCH_VIOLATION,
                    inputValue, constraintStringValue);

            case "enum" -> checkForViolation(isOneOf(inputValue, ruleValue),
                    ViolationMessage.RULE_ENUM_MATCH_VIOLATION,
                    inputValue, constraintStringValue);

            default -> throw new IllegalArgumentException(String.format(ViolationMessage.RULE_UNRECOGNIZED, ruleName));
        }
    }

    private boolean isOneOf(String inputValue, JsonNode ruleValue) {
        for (JsonNode enumValue : ruleValue.path("enum")) {
            if (inputValue.equals(enumValue.asText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getValidatorKey() {
        return STRING_VALIDATOR_KEY;
    }
}