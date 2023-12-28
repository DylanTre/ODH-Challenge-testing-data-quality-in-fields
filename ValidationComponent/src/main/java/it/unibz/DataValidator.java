package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;

import java.util.Iterator;
import java.util.List;

public class DataValidator {
    private final List<AbstractValidator> validators;
    private final JsonNode validationRules;
    private final boolean enableStrToPrimitive = false;

    public DataValidator(List<AbstractValidator> validators, JsonNode validationRules){
        this.validators = validators;
        this.validationRules = validationRules;
    }


    // Assumes that the input Map contains leaves
    public void validateAll(JsonNode dataToValidate) {
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

            for (AbstractValidator validator : validators) {
//                if () {
//                    System.out.println("found valid data for: " + entry.getKey() + ":" + entry.getValue().toString());
//                }

//                fieldValue.get(validator.getValidatorKey()
                validator.validate(fieldName, fieldValue, validationRules.get(validator.getValidatorKey()));

                System.out.println(validator.getViolations());
            }
        }
    }
}
