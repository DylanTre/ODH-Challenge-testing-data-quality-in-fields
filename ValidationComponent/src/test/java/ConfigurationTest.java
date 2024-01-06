import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.configuration.ConfigParser;
import it.unibz.validators.AbstractValidator;
import it.unibz.validators.ObjectValidator;
import it.unibz.validators.primitive.BooleanValidator;
import it.unibz.validators.primitive.DateTimeValidator;
import it.unibz.validators.primitive.NumberValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConfigurationTest {
    public static final String TEST_RULE_CONFIG_FILENAME = "test-rule-config.json";

    private Map<String, AbstractValidator> validators;

    @Before
    public void setUp() throws IOException {
        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(TEST_RULE_CONFIG_FILENAME);
        validators = config.getGenericValidators(validationRules);
    }

    @Test
    public void whenGetGenericValidatorsThenTheirCountMustEqualToThatInFile() {
        assertEquals(4, validators.size());
    }

    @Test
    public void whenGetGenericValidatorsThenTheirCountByTypeMustMatch() {
        int expectedInstances = 0;

        //TODO think of a better structure
        for (Map.Entry<String, AbstractValidator> validatorEntry : validators.entrySet()) {
            if (validatorEntry.getValue() instanceof NumberValidator)
                expectedInstances++;
            if (validatorEntry.getValue() instanceof DateTimeValidator)
                expectedInstances++;
            if (validatorEntry.getValue() instanceof BooleanValidator)
                expectedInstances++;
            if (validatorEntry.getValue() instanceof ObjectValidator)
                expectedInstances++;
        }
        assertEquals(validators.size(), expectedInstances);
    }
}
