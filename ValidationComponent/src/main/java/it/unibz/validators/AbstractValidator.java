package it.unibz.validators;


import it.unibz.utils.RegexUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
