package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class DataParser {

    public static JsonNode parseData(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();

        /*
         * The structure of the JSON is not fixed, and the keys can vary.
         */
        try {
            JsonNode node = objectMapper.readTree(new File(filename));

            return node;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
