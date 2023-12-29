package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.validators.AbstractValidator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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


    // Assumes that the input Map contains leaves
    public ObjectNode validateAll(JsonNode dataToValidate) {
        ObjectNode violations = objectMapper.createObjectNode();

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


            // FIXME for some reason overrides with null values existing violations
            validators.forEach((key, validator) ->
                    violations.setAll(validator.validate(fieldName, fieldValue) == null
                            ? objectMapper.createObjectNode()
                            : validator.validate(fieldName, fieldValue)));

//            validators.forEach((key, validator) ->
//                    System.out.println(key + "; " + validator.validate(fieldName, fieldValue)));
        }

        return violations;
    }
}
