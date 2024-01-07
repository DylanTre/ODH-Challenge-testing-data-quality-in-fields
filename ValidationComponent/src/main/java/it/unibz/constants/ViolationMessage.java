package it.unibz.constants;

/**
 * Class containing constant messages for violation scenarios in validation rules.
 */
public class ViolationMessage {

    /**
     * Message template for unrecognized rule.
     */
    public static final String RULE_UNRECOGNIZED = "Rule %s unrecognized";

    /**
     * Message template for name pattern violation.
     */
    public static final String RULE_NAME_PATTERN_VIOLATION = "Field %s name does not match pattern: %s";

    /**
     * Message template for "more than" violation.
     */
    public static final String RULE_MORE_THAN_VIOLATION = "Number %s is not more than %s";

    /**
     * Message template for "less than" violation.
     */
    public static final String RULE_LESS_THAN_VIOLATION = "Number %s is not less than %s";

    /**
     * Message template for "equal" violation.
     */
    public static final String RULE_EQUAL_VIOLATION = "Number %s is not equal to %s";

    /**
     * Message template for "even" violation.
     */
    public static final String RULE_EVEN_VIOLATION = "Number %s is not even";

    /**
     * Message template for "odd" violation.
     */
    public static final String RULE_ODD_VIOLATION = "Number %s is not odd";

    /**
     * Message template for value match violation.
     */
    public static final String RULE_VALUE_MATCH_VIOLATION = "Field %s value does not match value: %s";

    /**
     * Message template for enum match violation.
     */
    public static final String RULE_ENUM_MATCH_VIOLATION = "%s is none of the possible options";

    /**
     * Message template for day of week violation.
     */
    public static final String RULE_DAY_OF_WEEK_VIOLATION = "Date %s day of week is %s. Must be %s";

    /**
     * Message template for "is before" violation.
     */
    public static final String RULE_IS_BEFORE_VIOLATION = "Date %s is not before date %s";

    /**
     * Message template for "is after" violation.
     */
    public static final String RULE_IS_AFTER_VIOLATION = "Date %s is not after date %s";

    /**
     * Message template for expected value violation.
     */
    public static final String RULE_EXPECTED_VIOLATION = "%s was expected to be %s";

    /**
     * Message template for contains violation.
     */
    public static final String RULE_CONTAINS_VIOLATION = "%s does not contain %s";

    /**
     * Message template for array correct type violation.
     */
    public static final String RULE_ARRAY_CORRECT_TYPE_VIOLATION = "Array %s has more items than of type %s";
}
