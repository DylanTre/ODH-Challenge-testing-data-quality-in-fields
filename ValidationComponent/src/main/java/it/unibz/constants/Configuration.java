package it.unibz.constants;

import java.util.Locale;

/**
 * Constants related to validator configuration and validation data.
 * This class contains file names and default locale information used by the validator.
 */
public class Configuration {

    /**
     * The default configuration file name for validation rules.
     */
    public static final String RULE_CONFIGURATION_FILENAME = "rule-config.json";

    /**
     * The default input file name for validation data.
     */
    public static final String VALIDATION_INPUT_FILENAME = "validation-data.json";

    /**
     * The default output file name for validation results.
     */
    public static final String VALIDATION_OUTPUT_FILENAME = "validation-results.json";

    /**
     * The root locale to be used in the validation process.
     */
    public static final Locale ROOT_LOCALE = Locale.ROOT;
}

