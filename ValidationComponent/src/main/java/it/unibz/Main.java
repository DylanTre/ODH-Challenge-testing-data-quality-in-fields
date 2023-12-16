package it.unibz;

import it.unibz.configuration.ConfigParser;
import it.unibz.validators.ObjectValidator;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        /*
         * 1. Parse input
         * 2. Get constraints from config
         * 3. Validate for each
         */

        DataParser dataParser = new DataParser("validation-data.json");
        ConfigParser config = new ConfigParser();

        try {
            config.loadRules(ConfigParser.RULE_CONFIG_YML);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectValidator validator = new ObjectValidator(config.getValidationRules());


        validator.validateJsonNode(dataParser.parseData());

    }
}