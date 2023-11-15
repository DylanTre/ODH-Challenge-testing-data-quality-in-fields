package it.unibz;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractValidator<T> {

    /*
     * Data to validate comes as a JSON
     */
    public abstract boolean validate(T inputValue, Map<String, Object> rules);

}
