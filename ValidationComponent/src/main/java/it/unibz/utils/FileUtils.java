package it.unibz.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    private static final String FILE_WRITE_SUCCESS = "Validation results written to the file: %s";
    private static final String FILE_WRITE_ERROR = "Error writing to the file: %s";

    private FileUtils() {}

    /**
     * Writes the specified content to a file with the given file name.
     *
     * @param fileName       The name of the file to write the content to.
     * @param content        The content to be written to the file.
     *
     * @throws IllegalArgumentException If the fileName or content is null.
     */
    public static void writeToFile(String fileName, String content) throws IllegalArgumentException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            System.out.format(FILE_WRITE_SUCCESS, fileName);
        } catch (IOException e) {
            System.err.format(FILE_WRITE_ERROR, e.getMessage());
        }
    }

    /**
     * Writes the content of an ObjectNode to a JSON file using the Jackson library.
     *
     * @param fileName   The name of the file to which the ObjectNode should be written.
     * @param objectNode The ObjectNode containing the data to be written to the file.
     * @throws IllegalArgumentException If the fileName or objectNode is null.
     */
    public static void writeToFile(String fileName, ObjectNode objectNode) throws IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        try {
            objectWriter.writeValue(new File(fileName), objectNode);
            System.out.format(FILE_WRITE_SUCCESS, fileName);
        } catch (IOException e) {
            System.err.format(FILE_WRITE_ERROR, e.getMessage());
        }
    }
}
