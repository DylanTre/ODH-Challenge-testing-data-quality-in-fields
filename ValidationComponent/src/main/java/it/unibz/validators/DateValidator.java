package it.unibz.validators;

import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DateValidator extends AbstractValidator {

    @Setter
    private LocalDateTime parsedDate;

    public DateValidator(Map<String, Object> rules) {
        super(rules);
    }

    @Override
    public boolean validate(String key, Object inputValue) {
        String ruleDescriptorKey;
        Object ruleDescriptorValue;

        if (keyMatch(key)) {
            if (inputValue instanceof String dateInputValue) {
                boolean valid = true;
                for (Map.Entry<String, Object> entry : this.rules.entrySet()) {
                    ruleDescriptorKey = entry.getKey();
                    ruleDescriptorValue = entry.getValue();
                    /*
                     * FIXME:
                     *  Improve validation logic and
                     *  validation Scenarios
                     */
                    if ("format".equals(ruleDescriptorKey)) {
                        valid = isFormatValid(dateInputValue,
                                DateTimeFormatter.ofPattern((String) ruleDescriptorValue, Locale.ROOT));
                    }


                }
                return valid;
            }
            return false;
        }

        return false;
    }

    public boolean isFormatValid(String dateString, DateTimeFormatter acceptedFormat) {
        try {
            parsedDate = LocalDateTime.parse(dateString, acceptedFormat);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

}
