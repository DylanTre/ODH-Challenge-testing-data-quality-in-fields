package it.unibz;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class DataParser {

    private final String validationDataFilename;

    public  DataParser(final String validationDataFilename) {
        this.validationDataFilename = validationDataFilename;
    }

    public Map<String, Object> parseInputData() {
        var jsonFile = new File(validationDataFilename);
        var objectMapper = new ObjectMapper();

        /*
         * The structure of the JSON is not fixed, and the keys can vary.
         */
        try {
            return objectMapper.readValue(jsonFile, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
