package it.unibz;

import it.unibz.configuration.ConfigParser;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        /*
         * 1. Parse input
         * 2. Get rules from config
         * 3. Validate for each
         */

        DataParser dataParser = new DataParser("validation-data.json");
        ConfigParser config = new ConfigParser();

        try {
            config.loadRules(ConfigParser.RULE_CONFIG_YML);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataValidator dataValidator = new DataValidator(config.getGenericValidators());
        dataValidator.validateAll(dataParser.parseInputData());

    }
}