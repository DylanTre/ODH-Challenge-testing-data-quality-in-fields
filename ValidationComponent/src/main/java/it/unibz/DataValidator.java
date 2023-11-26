package it.unibz;

import it.unibz.utils.StringUtils;
import it.unibz.validators.AbstractValidator;

import java.util.List;
import java.util.Map;

public class DataValidator {
    private final List<AbstractValidator> validators;
    private final boolean enableStrToPrimitive = false;

    public DataValidator(List<AbstractValidator> validators){
        this.validators = validators;
    }


    //assumes that the input Map contains leaves
    public void validateAll(Map<String, Object> dataToValidate) {
        for (Map.Entry<String, Object> entry : dataToValidate.entrySet()) {
            Object value = entry.getValue();
            if(enableStrToPrimitive && (value instanceof String))
            {
                if(StringUtils.isBoolean((String) value)){
                    value = StringUtils.getBooleanFromString((String) value);
                }
                if(StringUtils.isNumber((String) value)){
                    value = StringUtils.getNumberFromString((String) value);
                }
            }

            for (AbstractValidator validator : validators) {
                 if (validator.validate(entry.getKey(), value)) {
                     System.out.println("found valid data for: " + entry.getKey() + ":" + entry.getValue().toString());
                 }
             }
        }
    }
}
