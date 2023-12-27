import it.unibz.configuration.ConfigParser;
import it.unibz.validators.BooleanValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class BooleanValidatorTest {
    public static final String BOOL_KEY = "boolean";

    public static final String TEST_RULE_CONFIG_YML = "test-rule-config.yml";

    private BooleanValidator booleanValidator;

    @Before
    public void setUp() throws FileNotFoundException {
        ConfigParser config = new ConfigParser();
        config.loadRules();
        booleanValidator = new BooleanValidator(new HashMap<>());
    }

    @Test
    public void booleanValidatorTrue(){
        booleanValidator.validate(BOOL_KEY, true, null);
    }
}
