package it.unibz.validators;


import it.unibz.configuration.ConfigParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//JsonLikeValue = JLString String | JLInt Int | JLArray [JsonLikeValue] deriving (Show, Eq)

public class ListValidator extends AbstractValidator {

    List<AbstractValidator> validators = new ArrayList<>();

    public ListValidator(Map<String, Object> rules) {
        super(rules);
        ConfigParser configParser = new ConfigParser();
        for (Map.Entry<String, Object> entry : rules.entrySet()) {
            if(entry.getValue() instanceof Map){
                AbstractValidator val = configParser.buildValidator(entry.getKey(), (Map<String, Object>) entry.getValue());
                if(null != val)
                    validators.add(val);

            }
        }
    }

    @Override
    public boolean validate(String key, Object inputValue) {
        boolean out = true;
        if(keyMatch(key)){
            if(inputValue instanceof List){
                for (Object entry : (List) inputValue)
                {
                    boolean innerMatch = false;
                    for (AbstractValidator validator : validators)
                    {
                        innerMatch = innerMatch || validator.validate(null, entry);
                    }
                    if (!innerMatch) {
                        out = false;
                        break;
                    }
                }
            }
        }
        return out;
    }
}
