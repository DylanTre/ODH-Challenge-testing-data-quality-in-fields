import com.fasterxml.jackson.databind.JsonNode;
import it.unibz.DataValidator;
import it.unibz.JsonParser;
import it.unibz.configuration.ConfigParser;
import it.unibz.constants.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class NumberValidatorTest {

    public static final String NUMBER_KEY = "number";
    public static final String MAGICAL_NUMBER_KEY = "magical_number";
    public static final String TEST_RULE_CONFIG_YML = "test-rule-config.yml";

    @Before
    public void setUp() throws IOException {
        ConfigParser config = new ConfigParser();
        JsonNode validationRules = config.loadValidationRules(TEST_RULE_CONFIG_YML);


        JsonNode dataToValidate = JsonParser.parseData(Configuration.VALIDATION_INPUT_FILENAME);
        DataValidator dataValidator = new DataValidator(config.getGenericValidators(validationRules));
    }


    @Test
    public void whenTestNumberThatMatchesKeyThenValidIfCorrespondingRulesSatisfied() {
        // Assert whether the rule structure is as expected?? Dunno
//        assertTrue(numberValidator.validate(MAGICAL_NUMBER_KEY, 28, ));
    }

}
