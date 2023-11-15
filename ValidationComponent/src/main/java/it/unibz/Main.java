package it.unibz;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        /*
         * 1. Parse input
         * 2. Get rules from config
         * 3. Validate for each
         */

        var dataParser = new DataParser("validation-data.json");
        var dataValidator = new DataValidator(new NumberValidator());

        try {
            dataValidator.loadRules();
            dataValidator.validateAll(dataParser.parseInputData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}