package it.unibz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringUtils {

    public static String TRUE_KW = "true";
    public static String FALSE_KW = "false";


    private StringUtils(){}

    public static boolean isNumber(String string){
        if (null == string) { return false; }
        try {
            final double d = Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isNumberPattern(String string){
        if (null == string) { return false; }
        final String numberPattern = "-?\\\\d+(\\\\.\\\\d+)?";
        return RegexUtils.regexMatch(numberPattern,string);
    }

    public static boolean isBoolean(String string){
        if (null == string) { return false; }
    return (TRUE_KW.equalsIgnoreCase(string) || FALSE_KW.equalsIgnoreCase(string));
    }

    public static Boolean getBooleanFromString(final String string)
    {
        if (!isBoolean(string)) return null; //FIXME should be agreed if we want exceptions or defensive approach
        return (TRUE_KW.equalsIgnoreCase(string));
    }

    public static Number getNumberFromString(String string) {
        if (!isNumberPattern(string)) return null; //FIXME should be agreed if we want exceptions or defensive approach
        return Double.parseDouble(string);
    }

    public static boolean isDateTime(String dateTimeString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException ex) {
            return false;
        }
        return true;
    }
}
