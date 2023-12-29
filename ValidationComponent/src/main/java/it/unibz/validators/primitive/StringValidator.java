package it.unibz.validators.primitive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.utils.RegexUtils;
import it.unibz.utils.StringUtils;
import it.unibz.validators.AbstractValidator;

public class StringValidator extends AbstractValidator<String> {

    private static final String STRING_VALIDATOR_KEY = "string";

    private static final String RULE_NAME_PATTERN_VIOLATION = "Field %s name does not match pattern: %s";
    private static final String RULE_VALUE_MATCH_VIOLATION = "Field %s value does not match value: %s";
    private static final String RULE_ENUM_MATCH_VIOLATION = "%s is none of the possible options";

    private final ArrayNode violationMessages;
    private final ObjectNode stringViolations;

    public StringValidator(JsonNode validationRules) {
        super(validationRules);

        this.violationMessages = getObjectMapper().createArrayNode();
        this.stringViolations = getObjectMapper().createObjectNode();
    }


    @Override
    public ObjectNode validate(final String key, JsonNode inputValue) {
        /*FIXME
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         * Expect dates and handle them! [Dates should be handled separately though] -- fixed
         */

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
        boolean valid;

        // toString() instead of textValue() because textValue() returns null for Numbers
        String constraintStringValue = ruleValue.toString();

        switch (ruleName) {
            case "key_match" -> {}

            // Example There should be possible to associate naming conventions
            //(using regular expressions) to validation rules.
            case "name_pattern" -> {
                valid = isValueMatch(key, constraintStringValue);
                checkForViolation(valid, String.format(RULE_NAME_PATTERN_VIOLATION, key, constraintStringValue));
            }
            case "value_match" -> {
                valid = isValueMatch(inputValue, constraintStringValue);
                checkForViolation(valid, String.format(RULE_VALUE_MATCH_VIOLATION, key, constraintStringValue));
            }
            case "enum" -> {
                valid = isOneOf(inputValue, ruleValue);
                checkForViolation(valid, String.format(RULE_ENUM_MATCH_VIOLATION, key));
            }
            default -> throw new IllegalArgumentException("Rule " + ruleName + "unrecognized");
        }
    }

    private boolean isValueMatch(String inputValue, String constraintStringValue) {
        return RegexUtils.regexMatch(constraintStringValue, inputValue);
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
    public void checkForViolation(boolean valid, String violationMessage) {
        if (!valid) {
            violationMessages.add(violationMessage);
        }
    }

    @Override
    public String getValidatorKey() {
        return STRING_VALIDATOR_KEY;
    }
}