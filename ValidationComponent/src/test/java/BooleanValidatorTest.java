import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.configuration.ConfigParser;
import it.unibz.validators.primitive.BooleanValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BooleanValidatorTest {
    public static final String TEST_RULE_CONFIG_FILENAME = "test-rule-config.json";

    private BooleanValidator booleanValidator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();

        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(TEST_RULE_CONFIG_FILENAME);

        // FIXME because of getGenericValidators, there we create validation specific rules for each
        //  but in this case we just want to use the basic boolean validator
        booleanValidator = new BooleanValidator(validationRules.get("boolean"));
    }

    @Test
    public void booleanValidatorTrue(){
        String inputKey = "bool";
        JsonNode inputValue = objectMapper.convertValue(true, JsonNode.class);

        ObjectNode validationResult = booleanValidator.validate(inputKey, inputValue);

        // Assert that violation list is empty
        assertEquals(getExpectedResult(objectMapper.createArrayNode()), validationResult);
    }

    @Test
    public void whenBooleanRuleWithoutKeyMatchThenApplyRuleToAllInputBoolean() {

    }

    private ObjectNode getExpectedResult(JsonNode innerValue) {
        ObjectNode node = objectMapper.createObjectNode();
        node.set(booleanValidator.getValidatorKey(), innerValue);
        return node;
    }
}
