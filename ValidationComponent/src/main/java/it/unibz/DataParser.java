package it.unibz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class DataParser {

    /**
     * Parses JSON data from a file and returns the corresponding JsonNode.
     *
     * @param filename The name of the file containing the JSON data to be parsed.
     * @return A JsonNode representing the parsed JSON data, or null if parsing fails.
     * @throws IOException If there is an issue reading the file.
     */
    public static JsonNode parseData(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File(filename));
    }
}
