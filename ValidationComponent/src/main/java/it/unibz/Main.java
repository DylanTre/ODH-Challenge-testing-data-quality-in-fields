package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.configuration.ConfigParser;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;

@RequiredArgsConstructor
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

//        var allValidationRules = config.getValidationRules();
//        NumberValidator numberValidator = new NumberValidator(new HashMap<>());
//        numberValidator.validate("numbero", 253, allValidationRules.get("number"));
//
//        System.out.println(numberValidator.getViolations());

        DataValidator dataValidator = new DataValidator(config.getGenericValidators(validationRules));

        /*
         * FIXME idea to call object validator directly to initiate validation since it
         *  goes through each primitive validator in recursion
         */

        System.out.println(dataValidator.validateAll(dataToValidate).toPrettyString());
    }
}