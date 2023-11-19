package it.unibz.configuration;

import it.unibz.validators.AbstractValidator;
import it.unibz.validators.BooleanValidator;
import it.unibz.validators.NumberValidator;
import it.unibz.validators.StringValidator;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigParser {
    public static final String RULE_CONFIG_YML = "rule-config.yml";

    private static final String GENERIC_VALIDATORS_KEY = "generic_rules";

    @Getter
    @Setter
    private Map<String, Map<String, Object>> validationRules;

    public void loadRules() throws FileNotFoundException {
        var yaml = new Yaml();
        var inputStream = new FileInputStream(RULE_CONFIG_YML);

        this.validationRules = yaml.load(inputStream);
    }

    public Map<String, Object> getRulesForSingleInputDataByKey(String inputDataKey) {
        return validationRules.get(inputDataKey);
    }

    public List<AbstractValidator> getGenericValidators(){
        List<AbstractValidator> validators = new ArrayList<AbstractValidator>();

        List<Map<String, Object>> genericRules = (List<Map<String, Object>>) validationRules.get(GENERIC_VALIDATORS_KEY);
        System.out.println();
        for (Map<String, Object> ob :genericRules) {
            //FIXME By contruction ob is size=1 this can be improved, to decide if changing the config structure or the code
            //still, this work but the code is ugly tho
            for (Map.Entry<String,Object> entry: ob.entrySet()){
                if("number".equals(entry.getKey())){
                    validators.add(new NumberValidator((Map<String, Object>) entry.getValue()));
                }else if("string".equals(entry.getKey())){
                    validators.add(new StringValidator((Map<String, Object>) entry.getValue()));
                }else if("boolean".equals(entry.getKey())){
                    validators.add(new BooleanValidator((Map<String, Object>) entry.getValue()));
                }
            }
        }
        return validators;
    }
}
