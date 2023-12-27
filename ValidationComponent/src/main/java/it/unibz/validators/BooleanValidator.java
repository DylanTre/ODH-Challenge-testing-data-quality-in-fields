package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BooleanValidator extends AbstractValidator {

    private static final String BOOLEAN_VIOLATION_KEY = "boolean";
    private final List<String> booleanViolations;
    public BooleanValidator(final Map<String, List<String>> violations)
    {
        super(violations);

        this.booleanViolations = new ArrayList<>();
    }

    @Override
    public boolean validate(final String key, Object inputValue, JsonNode singleTypeValidationRules) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */
        boolean out = true;
        if (keyMatch(, key)){
            if (inputValue instanceof Boolean){
                for (JsonNode node : this.constraints) {
                    if ("expected".equals(entry.getKey())){
                       out = (entry.getValue() == inputValue);
                    }
                }
            }
            return out;
        }
        violations.put(BOOLEAN_VIOLATION_KEY, booleanViolations);
        return false;
    }

    @Override
    public void resolveViolation(boolean valid, String violationMessage) {
        if (!valid) {
            booleanViolations.add(BOOLEAN_VIOLATION_KEY);
        }
    }
}
