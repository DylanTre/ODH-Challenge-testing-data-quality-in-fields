package it.unibz;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class Validator {

    public static final String RULE_CONFIG_YML = "rule-config.yml";

    public Map<String, Object> loadRules() throws FileNotFoundException {
        var yaml = new Yaml();
        var inputStream = new FileInputStream(RULE_CONFIG_YML);

        return yaml.load(inputStream);
    }

    /*
     * Data to validate comes as a JSON
     */
    public void validate() {
        try {
            var validationRules = loadRules();






        } catch (FileNotFoundException e) {
            // better to log for now just sysout
            System.out.println(e.getStackTrace());
        }
    }
}
