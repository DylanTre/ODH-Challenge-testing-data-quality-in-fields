package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.configuration.ConfigParser;
import it.unibz.configuration.ValidatorConstants;
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

        try {
            validationRules = config.loadValidationRules(ValidatorConstants.RULE_CONFIGURATION_FILENAME);

            JsonNode dataToValidate = DataParser.parseData(ValidatorConstants.VALIDATION_INPUT_FILENAME);
            DataValidator dataValidator = new DataValidator(config.getGenericValidators(validationRules));

            FileUtils.writeToFile(ValidatorConstants.VALIDATION_OUTPUT_FILENAME,
                    dataValidator.validateAll(dataToValidate));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}