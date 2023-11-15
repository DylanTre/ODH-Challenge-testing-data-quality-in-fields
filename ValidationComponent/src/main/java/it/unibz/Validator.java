package it.unibz;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Validator {
    public void loadRules() throws FileNotFoundException {
        var yaml = new Yaml();
        var inputStream = new FileInputStream(new File("rule-config.yml"));
        var obj = yaml.load(inputStream);
        System.out.println(obj);
    }
}
