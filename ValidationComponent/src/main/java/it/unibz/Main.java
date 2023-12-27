package it.unibz;

import it.unibz.configuration.ConfigParser;
import it.unibz.validators.NumberValidator;
import it.unibz.validators.ObjectValidator;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

@RequiredArgsConstructor
public class Main {
    public static void main(String[] args) {

        /*
         * 1. Parse input
         * 2. Get constraints from config
         * 3. Validate for each
         */
        ConfigParser config = new ConfigParser();

        try {
            config.loadRules();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectValidator validator = new ObjectValidator(DataParser.parseData("rule-config.json"));
        validator.validateJsonNode(DataParser.parseData("validation-data.json"));

        var allValidationRules = config.getValidationRules();
        NumberValidator numberValidator = new NumberValidator(new HashMap<>());
        numberValidator.validate("numbero", 253, allValidationRules.get("number"));

        System.out.println(numberValidator.getViolations());
    }
}