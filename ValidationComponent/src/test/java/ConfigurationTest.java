import it.unibz.configuration.ConfigParser;
import it.unibz.validators.AbstractValidator;
import it.unibz.validators.BooleanValidator;
import it.unibz.validators.DateValidator;
import it.unibz.validators.NumberValidator;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConfigurationTest {
    public static final String TEST_RULE_CONFIG_YML = "test-rule-config.yml";

    @Test
    public void loadAndCountValidators() throws FileNotFoundException {
        ConfigParser config = new ConfigParser();
        config.loadRules();

        List<AbstractValidator> validators = config.getGenericValidators();
        assertEquals(3, validators.size());
    }
    @Test
    public void loadAndCheckValidators() throws FileNotFoundException {
        ConfigParser config = new ConfigParser();
        config.loadRules();

        List<AbstractValidator> validators = config.getGenericValidators();
        int expectedInstances = 0;
        //TODO think of a better structure
        for (AbstractValidator v: validators) {
            if(v instanceof DateValidator)
                expectedInstances++;
            if(v instanceof NumberValidator)
                expectedInstances++;
            if(v instanceof BooleanValidator)
                expectedInstances++;
        }
        assertEquals(validators.size(), expectedInstances);
    }
}
