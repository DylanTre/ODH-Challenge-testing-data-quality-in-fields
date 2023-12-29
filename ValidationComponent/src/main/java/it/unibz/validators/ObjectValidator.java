package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.DataValidator;
import it.unibz.configuration.ConfigParser;

import java.util.Map;

public class ObjectValidator extends AbstractValidator<JsonNode> {

    private static final String OBJECT_VALIDATOR_KEY = "object";

    private final Map<String, AbstractValidator> validators;
    private final DataValidator dataValidator;
    private final ObjectNode objectViolations;

    public ObjectValidator(JsonNode validationRules) {
        super(validationRules);

        ConfigParser config = new ConfigParser();
        this.validators = config.getGenericValidators(getObjectRules());
        this.dataValidator = new DataValidator(validators);
        this.objectViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(String key, JsonNode inputValue) {
        if (JsonNodeType.OBJECT != inputValue.getNodeType()) {
            return null;
        }

        if (keyMatch(validationRules, key)) {
            objectViolations.putIfAbsent(getValidatorKey(), dataValidator.validateAll(inputValue));
        }

        return objectViolations;
    }

    @Override
    protected void applySingleRule(String key, JsonNode inputValue, String ruleName, JsonNode ruleValue) {
        /*
         * Object specific rules
         */
        if (ruleName.equals("key_match")) {
        }
    }

    @Override
    protected void checkForViolation(boolean valid, String violationMessage) {

    }

    @Override
    public String getValidatorKey() {
        return OBJECT_VALIDATOR_KEY;
    }

    private JsonNode getObjectRules() {
        if (validationRules.has("validators")) {
            return validationRules.get("validators");
        } else {
            return validationRules;
        }
    }

}
