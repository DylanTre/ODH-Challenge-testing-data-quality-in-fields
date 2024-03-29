package it.unibz.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for comparing {@code String} values using a regular expression
 */
public class RegexUtils {

    /*
     * preventing memory waste
     */
    private RegexUtils(){}

    /**
     * Checks if the specified value matches the given regular expression pattern.
     *
     * @param regex The regular expression pattern to match against.
     * @param value The value to be checked against the pattern.
     * @return {@code true} if the value matches the pattern; {@code false} otherwise.
     *         Returns {@code false} if either the regular expression or the value is null.
     */
    public static boolean regexMatch(final String regex, final String value){
        if (regex == null || value == null) {
            return false;
        }

        /*
         * Ensure that the regex matches the entire value as a word
         */
        String modifiedRegex = "^" + regex + "$";

        Pattern pattern = Pattern.compile(modifiedRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }
}
