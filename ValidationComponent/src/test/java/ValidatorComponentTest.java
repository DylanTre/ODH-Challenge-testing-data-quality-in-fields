import com.fasterxml.jackson.databind.JsonNode;
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
    public static final String ODH_INVALID_INPUT_FILENAME = "odh-input-invalid.json";
    public static final String ODH_VALID_INPUT_FILENAME = "odh-input-valid.json";

    private DataValidator dataValidator;
    private JsonNode dataToValidate;

    @Before
    public void setUp() {
        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(ODH_RULE_CONFIG_FILENAME);

        dataValidator = new DataValidator(config.getGenericValidators(validationRules));
    }

    @Test
    public void whenInputDataValidThenValidationOutputMustContainObjectNodeWithEmptyViolationMessageArrays() throws IOException {
        dataToValidate = JsonParser.parseData(ODH_VALID_INPUT_FILENAME);
        ObjectNode validationOutput = dataValidator.validateAll(dataToValidate);

        System.out.println(validationOutput);

        assertEquals(getValidOutput(), validationOutput);
    }

    @Test
    public void whenInputDataInvalidThenValidationOutputMustContainViolationMessages() throws IOException {
        dataToValidate = JsonParser.parseData(ODH_INVALID_INPUT_FILENAME);
        ObjectNode validationOutput = dataValidator.validateAll(dataToValidate);

        assertEquals(validationOutput, getInvalidOutput());
    }

    private ObjectNode getValidOutput() {
        return null;
    }

    private ObjectNode getInvalidOutput() {
        return null;
    }



}
