package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.configuration.ConfigParser;
import it.unibz.constants.Configuration;
import it.unibz.utils.FileUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        /*
         * 1. Parse input
         * 2. Get constraints from config
         * 3. Validate for each
         */
        ConfigParser config = new ConfigParser();
        JsonNode validationRules;

        validationRules = config.loadValidationRules(Configuration.RULE_CONFIGURATION_FILENAME);

        JsonNode dataToValidate;
        try {
            dataToValidate = JsonParser.parseData(Configuration.VALIDATION_INPUT_FILENAME);

            DataValidator dataValidator = new DataValidator(config.getGenericValidators(validationRules));

            FileUtils.writeToFile(Configuration.VALIDATION_OUTPUT_FILENAME,
                    dataValidator.validateAll(dataToValidate));
        } catch (IOException ex) {
            //
        }
    }
}