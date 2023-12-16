package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.ValidatorConstraint;
import it.unibz.model.ValidationRule;
import it.unibz.utils.RegexUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ObjectValidator {

    private static final String KEY_FILTER_VALUE = "key_match";

    @Getter
    private final Map<String, List<ValidationRule>> rules;
//
//    private final NumberValidator numberValidator;
//    private final StringValidator stringValidator;


    public void validateJsonNode(JsonNode jsonNode) {
        if (jsonNode.isEmpty()) {
            return;
        } else if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<String> fieldNames = objectNode.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNodeType fieldType = objectNode.get(fieldName).getNodeType();
                JsonNode fieldValue = objectNode.get(fieldName);

                // Perform validation based on the field name and value
                validateField(fieldType, fieldName, fieldValue, rules);

                // Recursive validation for nested objects
                if (fieldValue.isObject()) {
                    validateJsonNode(fieldValue);
                }
            }
        }
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
                               JsonNode nodeValue, Map<String, List<ValidationRule>> constraintValues) {
        if (nodeType == JsonNodeType.OBJECT) {
            return;
        }



        if (keyMatch(nodeName)) {
            switch (nodeType) {
//                case NUMBER -> numberValidator.validate(nodeName, nodeValue);
//                case BOOLEAN -> validateWithConstraints(null, nodeValue.textValue(), rules, null);
//                case STRING -> validateWithConstraints(null, nodeValue.textValue(), rules, null);
                default -> {
                    return;
                }
            }
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

    public boolean keyMatch(final String key) {
//        return RegexUtils.regexMatch(rules.get(KEY_FILTER_VALUE), key);
        return false;
    }


//    private void validateField(JsonNode jsonNode, String key, Object constraintValues) {
//        Validator validator;
//        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
//            validator = validatorFactory.getValidator();
//        }
//
//        if (jsonNode.has(key)) {
//            String fieldValue = jsonNode.get(key).asText();
//            if (!validateWithConstraints(validator, fieldValue, constraintValues, NumberValidator.class)) {
//                System.out.println(key + " validation failed");
//            } else {
//                System.out.println(key + " validation passed");
//            }
//        }
//    }

}
