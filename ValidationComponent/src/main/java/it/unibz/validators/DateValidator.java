package it.unibz.validators;

import it.unibz.exception.NotImplementedException;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class DateValidator extends AbstractValidator {

    private static final String DEFAULT_ITALIAN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    @Setter
    private LocalDateTime parsedDate;

    public DateValidator(Map<String, Object> rules) {
        super(rules);
    }

    @Override
    public boolean validate(String key, Object inputValue) {
        String ruleDescriptorKey;
        String ruleDescriptorValue;

        if (keyMatch(key)) {
            if (inputValue instanceof String dateInputValue) {
                for (Map.Entry<String, Object> entry : this.rules.entrySet()) {
                    ruleDescriptorKey = entry.getKey();
                    ruleDescriptorValue = (String) entry.getValue();
                    /*
                     * FIXME:
                     *  Improve validation logic and
                     *  validation Scenarios
                     *  ------------------------------------------------
                     *  Always parse the date first when checking format
                     *  only then can continue to check other date-related rules
                     */
                    if ("key_match".equals(ruleDescriptorKey)) {
                        // Ignore key match rule
                        continue;
                    } else if ("format".equals(ruleDescriptorKey)) {
                        if (!isFormatValid(dateInputValue, DateTimeFormatter.ofPattern(ruleDescriptorValue, Locale.ROOT))) {
                            break;
                        }
                    } else if ("day_of_week".equals(ruleDescriptorKey)) {
                        if (!isDateOfWeekValid(parsedDate, DayOfWeek.valueOf(ruleDescriptorValue))) {
                            break;
                        }
                    } else if ("is_before".equals(ruleDescriptorKey)) {
                        if (!isDateBeforeValid(parsedDate, ruleDescriptorValue)) {
                            break;
                        }
                    } else if ("is_after".equals(ruleDescriptorKey)) {
                        if (!isDateAfterValid(parsedDate, ruleDescriptorValue)) {
                            break;
                        }
                    } else {
                        throw new NotImplementedException("No method to validate this rule");
                    }

                }
                return true;
            }
            return false;
        }

        return false;
    }

    public boolean isFormatValid(String dateString, DateTimeFormatter acceptedFormat) {
        parsedDate = parseDate(dateString, acceptedFormat);
        return parsedDate != null;
    }


    public boolean isDateOfWeekValid(LocalDateTime parsedDate, DayOfWeek dayOfWeek) {
        return parsedDate.getDayOfWeek() == dayOfWeek;
    }

    /*
     * Accepted dates come as a String from a JSON object
     */
    public boolean isDateBeforeValid(LocalDateTime parsedDate, String acceptedBeforeDateString) {
        LocalDateTime parsedBeforeDate = parseDate(acceptedBeforeDateString,
                DateTimeFormatter.ofPattern(DEFAULT_ITALIAN_DATE_TIME_FORMAT));
        return parsedBeforeDate != null && parsedDate.isBefore(parsedBeforeDate);
    }

    public boolean isDateAfterValid(LocalDateTime parsedDate, String acceptedAfterDateString) {
        LocalDateTime parsedAfterDate = parseDate(acceptedAfterDateString,
                DateTimeFormatter.ofPattern(DEFAULT_ITALIAN_DATE_TIME_FORMAT));
        return parsedAfterDate != null && parsedDate.isAfter(parsedAfterDate);
    }

    private LocalDateTime parseDate(String dateToParse, DateTimeFormatter parseFormat) {
        try {
            return LocalDateTime.parse(dateToParse, parseFormat);
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
