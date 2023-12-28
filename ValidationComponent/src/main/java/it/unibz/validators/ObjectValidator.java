package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import it.unibz.configuration.ConfigParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectValidator extends AbstractValidator<Object> {

    private static final String OBJECT_VALIDATOR_KEY = "object";

    private final List<AbstractValidator> validators;
    private final List<String> ruleValidatorKeys;

    public ObjectValidator(Map<String, List<String>> violations) {
        super(violations);

        ConfigParser configParser = ConfigParser.getInstance();
        this.validators = configParser.getValidators();
        this.ruleValidatorKeys = new ArrayList<>();
    }


//    private void validateField(String fieldName, JsonNode fieldValue) {
//        Validator validator;
//        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
//            validator = validatorFactory.getValidator();
//
//
//        }
//
//        Set<ConstraintViolation<JsonNode>> violations = validator.validate(fieldValue);
//
//        if (!violations.isEmpty()) {
//            System.out.println("Validation failed for field '" + fieldName + "':");
//            for (ConstraintViolation<JsonNode> violation : violations) {
//                System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
//            }
//        }
//    }

    private void validateField(JsonNodeType nodeType, String nodeName,
                               JsonNode nodeValue, JsonNode constraintValues) {
        if (nodeType == JsonNodeType.OBJECT) {
            return;
        }



//        if (jsonNode.has("key")) {
//            String fieldValue = jsonNode.get(key).asText();
//            if (!validateWithConstraints(validator, fieldValue, constraintValues)) {
//                System.out.println(key + " validation failed");
//            } else {
//                System.out.println(key + " validation passed");
//            }
//        }

    }

    @Override
    public boolean validate(String key, JsonNode inputValue, JsonNode objectValidationRules) {
        if (JsonNodeType.OBJECT != inputValue.getNodeType()) {
            return false;
        }

        parseRules(objectValidationRules, key, inputValue);
        return false;
    }

    @Override
    protected void applySingleRule(Object inputValue, String ruleName, JsonNode ruleValue) {
        switch (ruleName) {
            case "key_match" -> {}
            case "validators" -> parseRules(ruleValue, inputValue);
        }
    }

    @Override
    protected void resolveViolation(boolean valid, String violationMessage) {

    }

    @Override
    public String getValidatorKey() {
        return OBJECT_VALIDATOR_KEY;
    }

    private void parseValidatorRule(JsonNode ruleValue) {
        if (ruleValue == null || ruleValue.isEmpty()) {
            return;
        }

        if (ruleValue.isArray()) {
            for (JsonNode innerNode : ruleValue) {
                // Assuming innerNode is in form of "validator_key"
                ruleValidatorKeys.add(innerNode.textValue());
            }
        }
    }

    private void dkak() {
        validators.forEach(validator -> {
            if (!OBJECT_VALIDATOR_KEY.equals(validator.getValidatorKey())) {
                if (ruleValidatorKeys)
            }
        });
    }
}
