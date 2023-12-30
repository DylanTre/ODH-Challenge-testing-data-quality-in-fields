package it.unibz.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.DataParser;
import it.unibz.enums.ValidatorType;
import it.unibz.exception.NotImplementedException;
import it.unibz.validators.AbstractValidator;
import it.unibz.validators.ArrayValidator;
import it.unibz.validators.ObjectValidator;
import it.unibz.validators.primitive.BooleanValidator;
import it.unibz.validators.primitive.DateValidator;
import it.unibz.validators.primitive.NumberValidator;
import it.unibz.validators.primitive.StringValidator;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Getter
public class ConfigParser {
    private final Map<String, AbstractValidator> validators;

    @Setter
    private JsonNode validationRules;

    public ConfigParser() {
        this.validators = new HashMap<>();
    }

    public JsonNode loadValidationRules(String filename) throws IOException {
        return DataParser.parseData(filename);
    }

    /**
     * Retrieves generic validators from the provided JSON node and returns them as a map.
     *
     * @param validationRules The JSON node containing validation rules for different validator types.
     * @return A map of validator keys to their corresponding AbstractValidator instances.
     * @throws NotImplementedException If a ValidatorType is encountered without a corresponding resolver.
     */
    public Map<String, AbstractValidator> getGenericValidators(JsonNode validationRules){
        for (ValidatorType validatorType : ValidatorType.values()) {
            String key = validatorType.getValidatorKey();
            if (validationRules.get(key) != null) {
                /*
                 * For each validator, assignValidation rules based on type (key)
                 */
                validators.put(key, resolveValidator(validatorType, validationRules.get(key)));
            }
        }

        return validators;
    }

    /**
     * Resolves and returns the appropriate AbstractValidator based on the provided ValidatorType
     * and its corresponding validation rules.
     *
     * @param validatorType The type of validator to be resolved.
     * @param validationRules The JSON node containing validation rules for the specified validator type.
     * @return An instance of AbstractValidator corresponding to the provided validator type.
     * @throws NotImplementedException If a ValidatorType is encountered without a corresponding resolver.
     */
    private AbstractValidator resolveValidator(ValidatorType validatorType, JsonNode validationRules) {
        return switch (validatorType) {
            case NUMBER -> new NumberValidator(validationRules);
            case STRING -> new StringValidator(validationRules);
            case BOOLEAN -> new BooleanValidator(validationRules);
            case DATE -> new DateValidator(validationRules);
            case OBJECT -> new ObjectValidator(validationRules);
            case ARRAY -> new ArrayValidator(validationRules);
            default -> throw new NotImplementedException(String.format("Validator of type %s undefined",
                    validatorType));
        };
    }
}
