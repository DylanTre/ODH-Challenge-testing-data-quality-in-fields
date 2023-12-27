package it.unibz.validators;


import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.utils.RegexUtils;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public abstract class AbstractValidator {

    protected final String KEY_FILTER_VALUE = "key_match";

    @Getter
    protected final Map<String, List<String>> violations;
    protected AbstractValidator(final Map<String, List<String>> violations){
        this.violations = violations;
    }

    /*
     * Data to validate comes as a JSON
     */
    public abstract boolean validate(String key, Object inputValue, JsonNode singleTypeValidationRules);
    public abstract void resolveViolation(boolean valid, String violationMessage);

    public boolean keyMatch(final JsonNode node, final String key) {
        return RegexUtils.regexMatch(node.get(KEY_FILTER_VALUE).textValue(), key);
    }

}
