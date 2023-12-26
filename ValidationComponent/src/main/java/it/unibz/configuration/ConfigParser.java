package it.unibz.configuration;

import it.unibz.validators.*;
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
    public static final String GENERIC_VALIDATORS_KEY = "generic_rules";

    @Getter
    @Setter
    private Map<String, Map<String, Object>> validationRules;

    public void loadRules(String filename) throws FileNotFoundException {
        var yaml = new Yaml();
        var inputStream = new FileInputStream(filename);

        this.validationRules = yaml.load(inputStream);
    }

    public Map<String, Object> getRulesForSingleInputDataByKey(String inputDataKey) {
        return validationRules.get(inputDataKey);
    }

    public List<AbstractValidator> getGenericValidators(){
        return getValidatorsFromMap(validationRules);
    }

    public List<AbstractValidator> getValidatorsFromMap(Map<String, Map<String, Object>> map){
        List<AbstractValidator> validators = new ArrayList<>();
        //FIXME By contruction ob is size=1 this can be improved, to decide if changing the config structure or the code
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
            validators.add(buildValidator(entry.getKey(), entry.getValue()));
        }
        return validators;
    }

    public List<AbstractValidator> getValidatorsFromList(List<Map<String, Object>> list){
        List<AbstractValidator> validators = new ArrayList<>();
        for (Map<String, Object> l : list){
            for (Map.Entry<String, Object> entry : l.entrySet()){
                validators.add(buildValidator(entry.getKey(), (Map<String, Object>) entry.getValue()));
            }
        }
        return validators;
    }

    public  AbstractValidator buildValidator(String key, Map<String, Object> content){
        if("number".equals(key)){
            return new NumberValidator(content);
        } else if("string".equals(key)){
            return new StringValidator(content);
        } else if("boolean".equals(key)){
            return new BooleanValidator(content);
        } else if("date".equals(key)){
            return new DateValidator(content);
        } else if ("object".equals(key)){
            return new ObjectValidator(content);
        }
        throw new IllegalArgumentException("Validator unavailable with name: " + key);
    }

}
