package it.unibz;

import java.util.Map;

public class NumberValidator extends Validator<Number> {

    public NumberValidator(DataParser dataParser, NumberValidator numberValidator) {
        super(dataParser, numberValidator);
    }


    @Override
    public boolean validate(Number inputValue, Map<String, Object> rules) {
        if (inputValue instanceof Integer intInputValue) {
            var valid = true;
            for (Map.Entry<String, Object> entry : rules.entrySet()) {
                /*
                 * Very basic hardcoded logic
                 * MUST rewrite after
                 * The same rules have to apply for every type of Number
                 */
                if (entry.getKey().equals("positive")) {
                    if (entry.getValue().equals("true")) {
                        valid = intInputValue >= 0;
                    } else {
                        valid = intInputValue < 0;
                    }
                } else if (entry.getKey().equals("more_than")) {
                    valid = valid && intInputValue > (Integer) entry.getValue();
                }
            }
            return valid;
        }


        return false;
    }
}
