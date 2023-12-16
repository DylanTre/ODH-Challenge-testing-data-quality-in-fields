package it.unibz.validators;

import it.unibz.utils.RegexUtils;

import java.util.Map;

public class StringValidator extends AbstractValidator{

    public StringValidator(final Map<String, Object> rules)
    {
        super(rules);
    }

    @Override
    public boolean validate(final String key, Object inputValue) {
        /*
         * Boolean variable should not have more than 1 rule, otherwise it is a list of boolean variables
         */
        if(keyMatch(key)){
            if(inputValue instanceof String){
                {
                    boolean valid = true;
                    for (Map.Entry<String, Object> entry : this.constraints.entrySet()) {
                        if("value_match".equals(entry.getKey())){ //FIXME String "value_match" should be a variable
                            valid = RegexUtils.regexMatch((String) constraints.get("value_match"), key);
                        }
                    }
                    return valid;
                }
            }
        }
        return false;
    }
}