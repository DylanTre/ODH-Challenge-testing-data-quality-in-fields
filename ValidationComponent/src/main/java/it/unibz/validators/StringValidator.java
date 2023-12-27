package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringValidator extends AbstractValidator {

    private static final String STRING_VIOLATION_KEY = "string";
    private final List<String> stringViolations;

    public StringValidator(Map<String, List<String>> violations) {
        super(violations);

        this.stringViolations = new ArrayList<>();
    }

    @Override
    public boolean validate(final String key, Object inputValue, JsonNode singleTypeValidationRules) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */
//        if(keyMatch(, key)){
//            if(inputValue instanceof String){
//                {
//                    boolean valid = true;
//                    for (JsonNode node : constraints) {
//                        if("value_match".equals(entry.getKey())){ //FIXME String "value_match" should be a variable
//                            valid = RegexUtils.regexMatch((String) constraints.get("value_match"), key);
//                        }
//                    }
//                    return valid;
//                }
//            }
//        }
        violations.put(STRING_VIOLATION_KEY, stringViolations);
        return false;
    }

    @Override
    public void resolveViolation(boolean valid, String violationMessage) {
        if (!valid) {
            stringViolations.add(violationMessage);
        }
    }
}