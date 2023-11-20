import it.unibz.configuration.ConfigParser;
import it.unibz.validators.DateValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class DateValidatorTest {

    public DateValidator dateValidator;
    public ConfigParser config = new ConfigParser();

    @Before
    public void setUp() throws FileNotFoundException {
        config = new ConfigParser();
        config.loadRules(ConfigParser.TEST_RULE_CONFIG_YML);
        dateValidator = new DateValidator(config.getRulesForSingleInputDataByKey("date"));
    }

    @Test
    public void whenValidateDateThenFormatMustBeExactAsInRuleSpecified() {
        String inputStringDate = "19/11/2023 12:00:00";
        DateTimeFormatter acceptedFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        assertTrue(dateValidator.isFormatValid(inputStringDate, acceptedFormat));
    }
}
