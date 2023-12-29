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

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


@Getter
public class ConfigParser {
    public static final String RULE_CONFIG_FILENAME = "rule-config.json";
    public static final String GENERIC_VALIDATORS_KEY = "generic_rules";

    private final Map<String, AbstractValidator> validators;

    @Setter
    private JsonNode validationRules;

    public ConfigParser() {
        this.validators = new HashMap<>();
    }

//    public synchronized static ConfigParser getInstance() {
//        if (instance == null) {
//            instance = new ConfigParser();
//        }
//        return instance;
//    }

    public JsonNode loadValidationRules() throws FileNotFoundException {
        return DataParser.parseData(RULE_CONFIG_FILENAME);
    }

    public Map<String, AbstractValidator> getGenericValidators(JsonNode validationRules){

        //FIXME By construction ob is size=1 this can be improved, to decide if changing the config structure or the code

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
