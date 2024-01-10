import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.configuration.ConfigParser;
import it.unibz.validators.primitive.NumberValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NumberValidatorTest {

    public static final String TEST_RULE_CONFIG_FILENAME = "test-rule-config.json";
    public static final String VALIDATOR_KEY = "number";

    private NumberValidator numberValidator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();

        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(TEST_RULE_CONFIG_FILENAME);
        numberValidator = new NumberValidator(validationRules.get(VALIDATOR_KEY));
    }


    @Test
    public void whenTestNumberThatMatchesKeyIfValidThenReturnEmptyViolations() {
        String inputKey = "magical_number";
        JsonNode inputValue = objectMapper.convertValue(10, JsonNode.class);

        ObjectNode validationResult = numberValidator.validate(inputKey, inputValue);
        assertEquals(getExpectedValidOutput(), validationResult);
    }

    @Test
    public void whenTestNumberThatMatchesKeyIfInvalidThenReturnViolationsObject() {
        String inputKey = "magical_number";
        JsonNode inputValue = objectMapper.convertValue(-21, JsonNode.class);

        ObjectNode validationResult = numberValidator.validate(inputKey, inputValue);
        assertEquals(getExpectedInvalidOutput(), validationResult);
    }

    private ObjectNode getExpectedValidOutput() {
        return objectMapper.createObjectNode();
    }
    private ObjectNode getExpectedInvalidOutput() {
        return objectMapper.createObjectNode();
    }

}
