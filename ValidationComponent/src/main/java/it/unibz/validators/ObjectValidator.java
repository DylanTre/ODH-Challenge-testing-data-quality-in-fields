package it.unibz.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.DataValidator;
import it.unibz.configuration.ConfigParser;

/**
 * Validator for object type.
 * <p>
 * Contains other primitive validators inside
 * <p>
 * Extends {@code AbstractValidator<JsonNode>} and provides specific validation
 * logic for object type.
 */
public class ObjectValidator extends AbstractValidator<JsonNode> {

    private static final String OBJECT_VALIDATOR_KEY = "object";

    private final DataValidator dataValidator;
    private final ObjectNode objectViolations;

    public ObjectValidator(JsonNode validationRules) {
        super(validationRules);

        ConfigParser config = new ConfigParser();
        this.dataValidator = new DataValidator(config.getGenericValidators(getObjectRules()));
        this.objectViolations = getObjectMapper().createObjectNode();
    }

    @Override
    public ObjectNode validate(String key, JsonNode inputValue) {
        if (JsonNodeType.OBJECT != inputValue.getNodeType()) {
            return null;
        }

        /*
         * key is null in case the object came in an ARRAY
         */
        if (key == null || keyMatch(validationRules, key)) {
            objectViolations.putIfAbsent(getValidatorKey(), dataValidator.validateAll(inputValue));
        }

        return objectViolations;
    }

    @Override
    protected void applySpecificValidationRule(String key, JsonNode inputValue, String ruleName, JsonNode ruleValue) {
        /*
         * Object specific rules
         */
    }

    @Override
    public String getValidatorKey() {
        return OBJECT_VALIDATOR_KEY;
    }

    /**
     * Retrieves the JsonNode representing the object rules from the validationRules.
     * <p>
     * This method checks if the validationRules has a "validators" property. If present,
     * it returns the JsonNode associated with "validators"; otherwise, it returns the entire
     * validationRules JsonNode.
     *
     * @return The JsonNode representing the object rules from the validationRules.
     */
    private JsonNode getObjectRules() {
        if (validationRules.has("validators")) {
            return validationRules.get("validators");
        } else {
            return validationRules;
        }
    }

}
