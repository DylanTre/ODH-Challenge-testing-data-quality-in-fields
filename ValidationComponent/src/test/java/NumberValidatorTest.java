import it.unibz.configuration.ConfigParser;
import it.unibz.validators.NumberValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class NumberValidatorTest {

    public static final String NUMBER_KEY = "number";
    public static final String MAGICAL_NUMBER_KEY = "magical_number";
    public static final String TEST_RULE_CONFIG_YML = "test-rule-config.yml";

    private NumberValidator numberValidator;

    @Before
    public void setUp() throws FileNotFoundException {
        ConfigParser config = new ConfigParser();
        config.loadRules();
        numberValidator = new NumberValidator(new HashMap<>());
    }


    @Test
    public void whenTestNumberThatMatchesKeyThenValidIfCorrespondingRulesSatisfied() {
        // Assert whether the rule structure is as expected?? Dunno
        assertTrue(numberValidator.validate(MAGICAL_NUMBER_KEY, 28, null));
    }

}
