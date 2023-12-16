package it.unibz.validators;

import java.util.Map;

public class NumberValidator extends AbstractValidator {

    private static final double TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE = 1e-10;

    public NumberValidator(final Map<String, Object> rules) {
        super(rules);
    }

    @Override
    public boolean validate(final String key, Object inputValue) {
        Number numericValue = (Number) inputValue;
        Object constraintValue = constraints.get(key);
        boolean floatingPoint = inputValue instanceof Double;

//        for (var rr : constraints.get(key)) {
//
//        }
        return false;
    }

    public boolean applySingleRule(Number fieldValue, String constraintKey, Object constraintValue, boolean floatingPoint) {
        return switch (constraintKey) {
            case "more_than" -> isMoreThan(fieldValue, (Number) constraintValue);
            case "less_than" -> !isMoreThan(fieldValue, (Number) constraintValue);
            case "equal" -> isEqual(fieldValue, (Number) constraintValue);
            case "even" -> (floatingPoint && (boolean) constraintValue) == isEven(fieldValue);
            case "odd" -> (floatingPoint && (boolean) constraintValue) != isEven(fieldValue);
            default -> throw new IllegalArgumentException("Rule " + constraintKey + "unrecognized");
        };
    }


    public boolean isMoreThan(Number number, Number numberToCompare) {
        return number.doubleValue() - numberToCompare.doubleValue() > 0;
    }

    public boolean isLessThan(Number number, Number numberToCompare) {
        return number.doubleValue() - numberToCompare.doubleValue() < 0;
    }

    public boolean isEqual(Number number, Number numberToCompare) {
        return Math.abs(number.doubleValue() - numberToCompare.doubleValue()) < TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE;
    }

    /*
     * This method serves both even and odd number cases
     */
    public boolean isEven(Number number) {
        return number != null && number.longValue() % 2 == 0;
    }

    public boolean isNaNOrInfinite(Double number) {
        return number != null && (Double.isNaN(number) || Double.isInfinite(number));
    }
}
