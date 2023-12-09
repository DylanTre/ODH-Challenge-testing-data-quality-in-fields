package it.unibz.validators;


import java.util.Map;

//JsonLikeValue = JLString String | JLInt Int | JLArray [JsonLikeValue] deriving (Show, Eq)

public class ListValidator extends AbstractValidator {


    protected ListValidator(Map<String, Object> rules) {
        super(rules);
    }

    @Override
    public boolean validate(String key, Object inputValue) {
        boolean out = true;
        if (keyMatch(key)){
            if (inputValue instanceof Boolean){
                for (Map.Entry<String, Object> entry : this.rules.entrySet()) {
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
