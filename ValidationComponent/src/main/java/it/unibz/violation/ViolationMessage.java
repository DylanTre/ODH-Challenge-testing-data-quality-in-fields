package it.unibz.violation;

public class ViolationMessage {

    public static final String RULE_UNRECOGNIZED = "Rule %s unrecognized";
    public static final String RULE_NAME_PATTERN_VIOLATION = "Field %s name does not match pattern: %s";
    public static final String RULE_MORE_THAN_VIOLATION = "Number %s is not more than %s";
    public static final String RULE_LESS_THAN_VIOLATION = "Number %s is not less than %s";
    public static final String RULE_EQUAL_VIOLATION = "Number %s is not equal to %s";
    public static final String RULE_EVEN_VIOLATION = "Number %s is not even";
    public static final String RULE_ODD_VIOLATION = "Number %s is not odd";
    public static final String RULE_VALUE_MATCH_VIOLATION = "Field %s value does not match value: %s";
    public static final String RULE_ENUM_MATCH_VIOLATION = "%s is none of the possible options";
    public static final String RULE_FORMAT_VIOLATION = "Date %s format invalid. Must be %s";
    public static final String RULE_DAY_OF_WEEK_VIOLATION = "Date %s day of week invalid. Must be %s";
    public static final String RULE_IS_BEFORE_VIOLATION = "Date %s is not before date %s";
    public static final String RULE_IS_AFTER_VIOLATION = "Date %s is not after date %s";
    public static final String RULE_EXPECTED_VIOLATION = "%s was expected to be %s";
    public static final String RULE_ARRAY_CONTAINS_VIOLATION = "Array %s does not contain %s";
    public static final String RULE_ARRAY_CORRECT_TYPE_VIOLATION = "Array %s has more items than of type %s";

}
