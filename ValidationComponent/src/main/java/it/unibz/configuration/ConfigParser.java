package it.unibz.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.DataParser;
import it.unibz.validators.AbstractValidator;
import it.unibz.validators.BooleanValidator;
import it.unibz.validators.DateValidator;
import it.unibz.validators.NumberValidator;
import it.unibz.validators.ObjectValidator;
import it.unibz.validators.StringValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ConfigParser {
    public static final String RULE_CONFIG_FILENAME = "rule-config.json";
    public static final String GENERIC_VALIDATORS_KEY = "generic_rules";

    private static ConfigParser instance;

    @Getter
    private final List<AbstractValidator> validators;

    // Private constructor to prevent instantiation from outside
    private ConfigParser() {
        validators = new ArrayList<>();
    }

    public synchronized static ConfigParser getInstance() {
        if (instance == null) {
            instance = new ConfigParser();
        }
        return instance;
    }

    @Getter
    @Setter
    private JsonNode validationRules;

    public void loadRules() throws FileNotFoundException {
        this.validationRules = DataParser.parseData(RULE_CONFIG_FILENAME);
    }

    public JsonNode getRulesForSingleInputDataByKey(String inputDataKey) {
        return validationRules.get(inputDataKey);
    }

    public List<AbstractValidator> getGenericValidators(){

        //FIXME By construction ob is size=1 this can be improved, to decide if changing the config structure or the code
//        still, this work but the code is ugly tho

        if (validationRules.get("number") != null) {
            validators.add(new NumberValidator(new HashMap<>()));
        }

        if (validationRules.get("string") != null) {
            validators.add(new StringValidator(new HashMap<>()));
        }

        if (validationRules.get("boolean") != null) {
            validators.add(new BooleanValidator(new HashMap<>()));
        }

        if (validationRules.get("date") != null) {
            validators.add(new DateValidator(new HashMap<>()));
        }

        if (validationRules.get("object") != null) {
            validators.add(new ObjectValidator(new HashMap<>()));
        }

        return validators;
    }
}
