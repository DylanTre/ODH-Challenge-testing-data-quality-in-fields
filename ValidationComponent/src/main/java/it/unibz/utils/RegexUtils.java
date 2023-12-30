package it.unibz.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    //preventing people to waste memory
    private RegexUtils(){}

    public static boolean regexMatch(final String regex, final String value){
        if (regex == null || value == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }
}
