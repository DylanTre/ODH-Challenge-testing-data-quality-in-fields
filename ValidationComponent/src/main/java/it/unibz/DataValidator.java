package it.unibz;

import it.unibz.validators.AbstractValidator;

import java.util.List;
import java.util.Map;

public class DataValidator {

   //Can be a singletone, if we decide so put the constructor to private and add a method for passing
   //validators dinamically instead of inside the constructor
   //private static DataValidator instance = null;
   //public static DataValidator getInstance(){
   //   if(null == instance)
   //       instance = new DataValidator();
   //  return instance;
   //}

    private final List<AbstractValidator> validators;

    public DataValidator(List<AbstractValidator> validators){
        this.validators = validators;
    }


    //assumes that the input Map contains leaves
    public void validateAll(Map<String, Object> dataToValidate) {
        for (Map.Entry<String, Object> entry : dataToValidate.entrySet()) {
             for (AbstractValidator validator : validators) {
                 if (validator.validate(entry.getKey(), entry.getValue())) {
                     System.out.println("found valid data for: " + entry.getKey() + ":" + entry.getValue().toString());
                 }
             }
        }
    }
}
