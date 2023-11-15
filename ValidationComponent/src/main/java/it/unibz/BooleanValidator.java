package it.unibz;

import java.util.Map;

public class BooleanValidator extends AbstractValidator<Boolean> {
    @Override
    public boolean validate(Boolean inputValue, Map<String, Object> rules) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */

        return false;
    }
}
