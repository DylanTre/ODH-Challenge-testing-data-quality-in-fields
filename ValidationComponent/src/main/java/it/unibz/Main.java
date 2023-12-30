package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.configuration.ConfigParser;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        /*
         * 1. Parse input
         * 2. Get constraints from config
         * 3. Validate for each
         */
        ConfigParser config = new ConfigParser();
        JsonNode validationRules = null;

        try {
            validationRules = config.loadValidationRules();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        JsonNode dataToValidate = DataParser.parseData("validation-data.json");
        DataValidator dataValidator = new DataValidator(config.getGenericValidators(validationRules));

        System.out.println(dataValidator.validateAll(dataToValidate).toPrettyString());
    }
}