import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.DataValidator;
import it.unibz.JsonParser;
import it.unibz.configuration.ConfigParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ValidatorComponentTest {

    public static final String ODH_RULE_CONFIG_FILENAME = "odh-rule-config.json";
    public static final String ODH_VALID_INPUT_FILENAME = "odh-input-valid.json";
    public static final String ODH_INVALID_INPUT_FILENAME = "odh-input-invalid.json";

    private DataValidator dataValidator;
    private JsonNode dataToValidate;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();

        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(ODH_RULE_CONFIG_FILENAME);

        dataValidator = new DataValidator(config.getGenericValidators(validationRules));
    }

    @Test
    public void whenInputDataValidThenValidationOutputMustContainEmptyObjectNode() throws IOException {
        dataToValidate = JsonParser.parseData(ODH_VALID_INPUT_FILENAME);
        ObjectNode validationOutput = dataValidator.validateAll(dataToValidate);

        assertEquals(getExpectedValidOutput(), validationOutput);
    }

    @Test
    public void whenInputDataInvalidThenValidationOutputMustContainViolationMessages() throws IOException {
        dataToValidate = JsonParser.parseData(ODH_INVALID_INPUT_FILENAME);
        ObjectNode validationOutput = dataValidator.validateAll(dataToValidate);

        assertEquals(getExpectedInvalidOutput(), validationOutput);
    }

    private ObjectNode getExpectedValidOutput() {
        return objectMapper.createObjectNode();
    }

    private ObjectNode getExpectedInvalidOutput() {
        return null;
    }

}
