import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.DataParser;
import it.unibz.DataValidator;
import it.unibz.configuration.ConfigParser;
import it.unibz.configuration.ValidatorConstants;
import it.unibz.validators.primitive.NumberValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class NumberValidatorTest {

    public static final String NUMBER_KEY = "number";
    public static final String MAGICAL_NUMBER_KEY = "magical_number";
    public static final String TEST_RULE_CONFIG_YML = "test-rule-config.yml";

    @Before
    public void setUp() throws IOException {
        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(TEST_RULE_CONFIG_YML);


        JsonNode dataToValidate = DataParser.parseData(ValidatorConstants.VALIDATION_INPUT_FILENAME);
        DataValidator dataValidator = new DataValidator(config.getGenericValidators(validationRules));
    }


    @Test
    public void whenTestNumberThatMatchesKeyThenValidIfCorrespondingRulesSatisfied() {
        // Assert whether the rule structure is as expected?? Dunno
        assertTrue(numberValidator.validate(MAGICAL_NUMBER_KEY, 28, ));
    }

}
