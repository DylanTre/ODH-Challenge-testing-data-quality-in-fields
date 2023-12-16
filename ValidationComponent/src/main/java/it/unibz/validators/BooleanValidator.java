package it.unibz.validators;

import java.util.Map;

public class BooleanValidator extends AbstractValidator {

    public BooleanValidator(final Map<String, Object> rules)
    {
        super(rules);
    }

    @Override
    public boolean validate(final String key, Object inputValue) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */
        boolean out = true;
        if (keyMatch(key)){
            if (inputValue instanceof Boolean){
                for (Map.Entry<String, Object> entry : this.constraints.entrySet()) {
                    if ("expected".equals(entry.getKey())){
                       out = (entry.getValue() == inputValue);
                    }
                }
            }
            return out;
        }
        return false;
    }
}
