package it.unibz;

import java.util.Map;

public class BooleanValidator extends Validator<Boolean>{
    public BooleanValidator(DataParser dataParser, NumberValidator numberValidator) {
        super(dataParser, numberValidator);
    }

    @Override
    public boolean validate(Boolean inputValue, Map<String, Object> rules) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */

        return false;
    }
}
