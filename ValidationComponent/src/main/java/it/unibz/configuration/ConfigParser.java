package it.unibz.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.DataParser;
import it.unibz.validators.AbstractValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class ConfigParser {
    public static final String RULE_CONFIG_FILENAME = "rule-config.json";
    public static final String GENERIC_VALIDATORS_KEY = "generic_rules";

    @Getter
    @Setter
    private JsonNode validationRules;

    public void loadRules() throws FileNotFoundException {
        this.validationRules = DataParser.parseData(RULE_CONFIG_FILENAME);
    }

    public JsonNode getRulesForSingleInputDataByKey(String inputDataKey) {
        return validationRules.get(inputDataKey);
    }

    public List<AbstractValidator> getGenericValidators(){
        List<AbstractValidator> validators = new ArrayList<>();

        //FIXME By construction ob is size=1 this can be improved, to decide if changing the config structure or the code
        //still, this work but the code is ugly tho
//        for (Map.Entry<String, List<ValidationRule>> entry : validationRules){
//            if("number".equals(entry.getKey())){
//                validators.add(new NumberValidator((Map<String, Object>) entry.getValue()));
//            } else if("string".equals(entry.getKey())){
//                validators.add(new StringValidator((Map<String, Object>) entry.getValue()));
//            } else if("boolean".equals(entry.getKey())){
//                validators.add(new BooleanValidator((Map<String, Object>) entry.getValue()));
//            } else if("date".equals(entry.getKey())){
//                validators.add(new DateValidator((Map<String, Object>) entry.getValue()));
//            }
//        }
        return validators;
    }
}
