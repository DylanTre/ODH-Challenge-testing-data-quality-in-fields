package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;

import java.util.Iterator;
import java.util.Map;

// FIXME Maybe it's just one big ObjectValidator?
public class DataValidator {
    private final Map<String, AbstractValidator> validators;
    private final ObjectMapper objectMapper;
    private final boolean enableStrToPrimitive = false;

    public DataValidator(Map<String, AbstractValidator> validators){
        this.validators = validators;
        this.objectMapper = new ObjectMapper();
    }


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

                if (enableStrToPrimitive && JsonNodeType.STRING == fieldValue.getNodeType()) {
                    String entryStringValue = fieldValue.toString();

//                if (StringUtils.isBoolean(entryStringValue)){
//                    fieldValue = StringUtils.getBooleanFromString(entryStringValue);
//                }
//                if (StringUtils.isNumber(entryStringValue)){
//                    fieldValue = StringUtils.getNumberFromString(entryStringValue);
//                }
                }

                applyValidators(fieldName, fieldValue, violations);
            }
        } else {
            applyValidators(null, dataToValidate, violations);
        }

        return violations;
    }

    private void applyValidators(String fieldName, JsonNode fieldValue, ObjectNode violations) {
        validators.forEach((s, validator) -> {
            ObjectNode validationResult = validator.validate(fieldName, fieldValue);
            violations.setAll(validationResult == null || validationResult.isEmpty()
                    ? objectMapper.createObjectNode()
                    : validationResult);

        });
    }
}
