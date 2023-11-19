package it.unibz.validators;

import java.util.Map;

public class NumberValidator extends AbstractValidator{

    public NumberValidator(final Map<String, Object> rules){
        super(rules);
    }

    @Override
    public boolean validate(final String key, Object inputValue) {
        if(keyMatch(key)){
            if (inputValue instanceof Integer intInputValue) {
                boolean valid = true;
                for (Map.Entry<String, Object> entry : this.rules.entrySet()) {
                    /*
                     * Very basic hardcoded logic
                     * MUST rewrite after
                     * The same rules have to apply for every type of Number
                     */
                    if (entry.getKey().equals("positive")) {
                        if ((Boolean) entry.getValue()) {
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
        }
        return false;
    }
}
