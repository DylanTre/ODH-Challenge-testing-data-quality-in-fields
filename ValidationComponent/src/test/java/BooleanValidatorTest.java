import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.configuration.ConfigParser;
import it.unibz.validators.primitive.BooleanValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BooleanValidatorTest {
    public static final String TEST_RULE_CONFIG_FILENAME = "test-rule-config.json";

    private BooleanValidator booleanValidator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();

        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(TEST_RULE_CONFIG_FILENAME);
        booleanValidator = new BooleanValidator(validationRules.get("boolean"));
    }

    @Test
    public void booleanValidatorTrue(){
        String inputKey = "bool";
        JsonNode inputValue = objectMapper.convertValue(true, JsonNode.class);

        ObjectNode validationResult = booleanValidator.validate(inputKey, inputValue);
        assertEquals(getExpectedValidOutput(), validationResult);
    }

    private ObjectNode getExpectedValidOutput() {
        return objectMapper.createObjectNode();
    }
}
