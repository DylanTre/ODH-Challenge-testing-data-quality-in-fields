import it.unibz.configuration.ConfigParser;
import it.unibz.validators.primitive.DateValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateValidatorTest {

    public static final String DATE_KEY = "date";
    public static final String FIXED_INSTANT = "2023-11-20T12:00:00Z";
    public static final String DATE_ENTRY_KEY = "datetino";

    public static final String TEST_RULE_CONFIG_YML = "test-rule-config.yml";

    private Clock clock;
    private DateValidator dateValidator;

    @Before
    public void setUp() throws FileNotFoundException {
        clock = Clock.fixed(Instant.parse(FIXED_INSTANT), ZoneId.of("UTC"));
        ConfigParser config = new ConfigParser();
        config.loadValidationRules();
        dateValidator = new DateValidator(new HashMap<>());
    }

    @Test
    public void whenTestNumberThatMatchesKeyThenValidIfCorrespondingRulesSatisfied() {
        // FIXME Assert whether the rule structure is as expected?? Dunno
        assertTrue(dateValidator.validate(DATE_ENTRY_KEY, "20/11/2023 12:00:00", ));
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
