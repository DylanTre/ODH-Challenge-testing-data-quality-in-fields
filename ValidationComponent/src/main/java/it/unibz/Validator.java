package it.unibz;


import it.unibz.exception.NotImplementedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@RequiredArgsConstructor
public abstract class Validator<T> {

    public static final String RULE_CONFIG_YML = "rule-config.yml";

    private final DataParser dataParser;
    private final NumberValidator numberValidator;

    @Getter
    @Setter
    private Map<String, Map<String, Object>> validationRules;

    public Map<String, Map<String, Object>> loadRules() throws FileNotFoundException {
        var yaml = new Yaml();
        var inputStream = new FileInputStream(RULE_CONFIG_YML);

        return yaml.load(inputStream);
    }


    public Map<String, Object> getRulesForSingleInputDataByKey(String inputDataKey) {
        return validationRules.get(inputDataKey);
    }

    public void initializeValidation() throws FileNotFoundException {
        var dataToValidate = dataParser.parseValidationData();
        var validationRules = loadRules();

        for (Map.Entry<String, Object> entry : dataToValidate.entrySet()) {
            System.out.println(entry + " valid? " + validateData(entry.getValue(),
                    getRulesForSingleInputDataByKey(entry.getKey())));
        }
    }

    private boolean validateData(Object entryValue, Map<String, Object> singleDataRules) throws NotImplementedException {
        if (entryValue instanceof Number) {
            /*
             * Validate number implementing types
             */
            return numberValidator.validate((Number) entryValue, singleDataRules);

        } else {
            throw new NotImplementedException("Validation for this type not yet implemented");
        }
    }

    /*
     * Data to validate comes as a JSON
     */
    public abstract boolean validate(T inputValue, Map<String, Object> rules);

}
