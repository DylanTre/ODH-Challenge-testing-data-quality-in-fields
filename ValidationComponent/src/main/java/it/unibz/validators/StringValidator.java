package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import it.unibz.utils.RegexUtils;
import it.unibz.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringValidator extends AbstractValidator<String> {

    private static final String STRING_VALIDATOR_KEY = "string";

    private static final String RULE_VALUE_MATCH_VIOLATION = "String %s does not match value: %s";
    private static final String RULE_ENUM_MATCH_VIOLATION = "String %s is none of the possible options";

    private final List<String> stringViolations;

    public StringValidator(Map<String, List<String>> violations) {
        super(violations);
        this.stringViolations = new ArrayList<>();
    }

    @Override
    public boolean validate(final String key, JsonNode inputValue, JsonNode stringValidationRules) {
        /*FIXME
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         * Expect dates and handle them! [Dates should be handled separately though] -- fixed
         */

        if (JsonNodeType.STRING != inputValue.getNodeType()) {
            return false;
        }

        String stringValue = inputValue.textValue();

        // FIXME solve the changing pattern problem
        //  set DEFAULT pattern somehow
        if (StringUtils.isDateTime(stringValue, "yyyy-MM-dd HH:mm:ss")) {
            return false;
        }

        parseRules(stringValidationRules, key, stringValue);
        return false;
    }

    @Override
    public void applySingleRule(String inputValue, String ruleName, JsonNode ruleValue) {
        boolean valid;
        String constraintStringValue = ruleValue.textValue();

        switch (ruleName) {
            case "key_match" -> {}
            case "value_match" -> {
                valid = isValueMatch(inputValue, constraintStringValue);
                resolveViolation(valid, String.format(RULE_VALUE_MATCH_VIOLATION, inputValue, constraintStringValue));
            }
            case "enum" -> {
                valid = isOneOf(inputValue, ruleValue);
                resolveViolation(valid, String.format(RULE_ENUM_MATCH_VIOLATION, inputValue));
            }
            default -> throw new IllegalArgumentException("Rule " + ruleName + "unrecognized");
        }

        violations.put(STRING_VALIDATOR_KEY, stringViolations);
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
    public void resolveViolation(boolean valid, String violationMessage) {
        if (!valid) {
            stringViolations.add(violationMessage);
        }
    }

    @Override
    public String getValidatorKey() {
        return STRING_VALIDATOR_KEY;
    }
}