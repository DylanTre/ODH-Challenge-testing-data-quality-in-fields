package it.unibz.validators;


import it.unibz.utils.RegexUtils;
import lombok.Getter;
import java.util.Map;

public abstract class AbstractValidator {

    protected final String KEY_FILTER_VALUE = "key_match";

    @Getter
    protected final Map<String, Object> rules;
    protected AbstractValidator(final Map<String, Object> rules){
        this.rules = rules;
    }

    /*
     * Data to validate comes as a JSON
     */
    public abstract boolean validate(String key, Object inputValue);

    public boolean keyMatch(final String key){
        return RegexUtils.regexMatch((String) rules.get(KEY_FILTER_VALUE), key);
    }
}
