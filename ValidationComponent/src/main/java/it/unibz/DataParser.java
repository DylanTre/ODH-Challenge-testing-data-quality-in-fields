package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class DataParser {

    private final String validationDataFilename;

    public  DataParser(final String validationDataFilename) {
        this.validationDataFilename = validationDataFilename;
    }

    public JsonNode parseData() {
        var jsonFile = new File(validationDataFilename);
        var objectMapper = new ObjectMapper();

        /*
         * The structure of the JSON is not fixed, and the keys can vary.
         */
        try {
            return objectMapper.readTree(jsonFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
