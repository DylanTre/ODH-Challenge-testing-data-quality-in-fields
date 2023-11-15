package it.unibz;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Map;

@RequiredArgsConstructor
public class DataParser {

    private final String validationDataFilename;

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
