import it.unibz.configuration.ConfigParser;
import it.unibz.validators.NumberValidator;
import org.junit.Before;

import java.io.FileNotFoundException;

public class NumberValidatorTest {
    private NumberValidator numberValidator;

    @Before
    public void setUp() throws FileNotFoundException {
        ConfigParser config = new ConfigParser();
        config.loadRules(ConfigParser.TEST_RULE_CONFIG_YML);
        numberValidator = new NumberValidator(config.getRulesForSingleInputDataByKey("date"));
    }
}
