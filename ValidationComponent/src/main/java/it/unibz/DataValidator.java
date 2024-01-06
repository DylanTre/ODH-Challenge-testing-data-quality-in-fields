package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;

import java.util.Iterator;
import java.util.Map;

/**
 * Utility class for validating data.
 * <p>
 * Entry point to JSON input validation.
 */
public class DataValidator {
    private final Map<String, AbstractValidator> validators;
    private final ObjectMapper objectMapper;

    public DataValidator(Map<String, AbstractValidator> validators){
        this.validators = validators;
        this.objectMapper = new ObjectMapper();
    }


    /**
     * Validates all fields in the provided JsonNode data.
     * <p>
     * This method iterates through the fields of the dataToValidate JsonNode and applies
     * registered validators to each field. The validation results are collected and returned
     * as an ObjectNode containing violation details.
     *
     * @param dataToValidate The JsonNode data to be validated.
     * @return An ObjectNode containing violation details, if any violations are found.
     */
    public ObjectNode validateAll(JsonNode dataToValidate) {
        ObjectNode violations = objectMapper.createObjectNode();

        if (dataToValidate == null) {
            return violations;
        }

        if (dataToValidate.isObject()) {
            ObjectNode objectNode = (ObjectNode) dataToValidate;
            Iterator<String> fieldNames = objectNode.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = objectNode.get(fieldName);
                applyValidators(fieldName, fieldValue, violations);
            }
        } else {
            applyValidators(null, dataToValidate, violations);
        }

        return violations;
    }

    /**
     * Applies registered validators to a specific field and adds the validation results to the violations ObjectNode.
     * <p>
     * This method iterates through registered validators and applies them to the specified field.
     * The validation results are added to the violations ObjectNode.
     *
     * @param fieldName   The name of the field being validated.
     * @param fieldValue  The value of the field being validated.
     * @param violations  The ObjectNode containing violation details.
     */
    public void applyValidators(String fieldName, JsonNode fieldValue, ObjectNode violations) {
        validators.forEach((validatorKey, validator) -> {
            ObjectNode validationResult = validator.validate(fieldName, fieldValue);
            violations.setAll(validationResult == null || validationResult.isEmpty()
                    ? objectMapper.createObjectNode()
                    : validationResult);
        });
    }

}
