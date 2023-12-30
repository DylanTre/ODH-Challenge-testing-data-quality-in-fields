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
//        config.loadValidationRules();
//        dateValidator = new DateValidator(dateValidator.validate());
    }


}
