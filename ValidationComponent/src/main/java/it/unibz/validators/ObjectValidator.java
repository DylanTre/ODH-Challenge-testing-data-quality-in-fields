package it.unibz.validators;

import it.unibz.configuration.ConfigParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectValidator extends AbstractValidator {

    List<AbstractValidator> validators = new ArrayList<>();

    public ObjectValidator(final Map<String, Object> rules)
    {
        super(rules);
        ConfigParser configParser = new ConfigParser();
        this.validators = configParser.getValidatorsFromList((List<Map<String, Object>>) rules.get("validators"));
    }

    @Override
    public boolean validate(final String key, Object inputValue) {
        boolean out = true;
        if (keyMatch(key)){
            if (inputValue instanceof Map){
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) inputValue).entrySet())
                {
                    boolean innerMatch = false;
                    for (AbstractValidator validator : validators)
                    {
                        innerMatch = innerMatch || validator.validate(entry.getKey(), entry.getValue());
                    }
                    if (!innerMatch) {
                        out = false;
                        break;
                    }
                }
            }
            return out;
        }
        return false;
    }
}
