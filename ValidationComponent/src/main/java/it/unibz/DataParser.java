package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataParser {

    /**
     * Parses JSON data from a file and returns the corresponding JsonNode.
     * Assumes the file is located in resources folder
     *
     * @param filename The name of the file containing the JSON data to be parsed.
     * @return A JsonNode representing the parsed JSON data, or null if parsing fails.
     * @throws IOException If there is an issue reading the file.
     */
    public static JsonNode parseData(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = DataParser.class.getResourceAsStream(filename);
        return objectMapper.readTree(inputStream);
    }
}
