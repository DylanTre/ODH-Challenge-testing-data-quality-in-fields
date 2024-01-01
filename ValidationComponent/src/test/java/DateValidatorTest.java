import it.unibz.configuration.ConfigParser;
import it.unibz.validators.primitive.DateValidator;
import org.junit.Before;

import java.io.FileNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

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
