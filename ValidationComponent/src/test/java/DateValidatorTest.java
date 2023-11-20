import it.unibz.configuration.ConfigParser;
import it.unibz.validators.DateValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateValidatorTest {

    private Clock clock;
    private DateValidator dateValidator;

    @Before
    public void setUp() throws FileNotFoundException {
        clock = Clock.fixed(Instant.parse("2023-11-20T12:00:00Z"), ZoneId.of("UTC"));
        ConfigParser config = new ConfigParser();
        config.loadRules(ConfigParser.TEST_RULE_CONFIG_YML);
        dateValidator = new DateValidator(config.getRulesForSingleInputDataByKey("date"));
    }

    @Test
    public void whenValidateDateThenFormatMustBeExactAsRuleSpecified() {
        String inputStringDate = "19/11/2023 12:00:00";
        DateTimeFormatter acceptedFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        assertTrue(dateValidator.isFormatValid(inputStringDate, acceptedFormat));
    }

    @Test
    public void whenValidateDateThenDayOfWeekMustBeExactAsRuleSpecified() {
        assertTrue(dateValidator.isDateOfWeekValid(LocalDateTime.now(clock), DayOfWeek.MONDAY));
    }

    @Test
    public void whenValidateBeforeDateThenDateMustBeBeforeRuleSpecifiedDate() {
        assertTrue(dateValidator.isDateBeforeValid(LocalDateTime.now(clock), "23/11/2023 12:00:00"));
    }

    @Test
    public void whenValidateAfterDateThenDateMustBeAfterRuleSpecifiedDate() {
        assertTrue(dateValidator.isDateAfterValid(LocalDateTime.now(clock), "18/11/2023 12:00:00"));
    }

    @Test
    public void whenValidateAfterDateThenInvalidIfDateIsBeforeRuleSpecifiedDate() {
        assertFalse(dateValidator.isDateAfterValid(LocalDateTime.now(clock), "20/11/2023 12:01:00"));
    }
}
