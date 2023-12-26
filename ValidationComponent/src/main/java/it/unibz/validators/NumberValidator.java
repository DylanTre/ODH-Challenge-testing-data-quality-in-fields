package it.unibz.validators;

import java.util.Map;

public class NumberValidator extends AbstractValidator {

    private static final double TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE = 1e-10;

    public NumberValidator(final Map<String, Object> rules) {
        super(rules);
    }

    @Override
    public boolean validate(final String key, Object inputValue) {
        if (keyMatch(key)) {
            if (inputValue == null) {
                return false;
            }

            if (inputValue instanceof Integer) {
                return validate((Integer)inputValue, false);
            } else if (inputValue instanceof Long) {
                return validate((Long)inputValue, false);
            } else if (inputValue instanceof Double) {
                if (isNaNOrInfinite((Double)inputValue)) {
                    return false;
                }
                return validate((Double)inputValue, true);
            }
            return false;
        }
        return false;
    }

    private <T extends Number> boolean validate(T number, boolean isFloatingPoint) {
        boolean valid = true;
        for (Map.Entry<String, Object> entry : this.rules.entrySet()) {
            /*
             *FIXME
             * Very basic hardcoded logic
             * MUST rewrite after
             * The same rules have to apply for every type of Number
             */

            if ("key_match".equals(entry.getKey())) {
                continue;
            }

            if ("more_than".equals(entry.getKey())) {
                if (!isMoreThan(number, (Number) entry.getValue())) {
                    valid = false;
                    break;
                }
            } else if ("less_than".equals(entry.getKey())) {
                if (!isLessThan(number, (Number) entry.getValue())) {
                    valid = false;
                    break;
                }
            }

            if (isFloatingPoint) {
                if ("equal".equals(entry.getKey())) {
                    if (!isFloatingPointEqual((Double) number, (Double) entry.getValue())) {
                        valid = false;
                        break;
                    }
                }
            } else {
                if ("equal".equals(entry.getKey())) {
                    if (!isEqual(number, (Number) entry.getValue())) {
                        valid = false;
                        break;
                    }
                }
                else if ("even".equals(entry.getKey())) {
                    if (!isEven(number)) {
                        valid = false;
                        break;
                    }
                } else if ("odd".equals(entry.getKey())) {
                    if (isEven(number)) {
                        valid = false;
                        break;
                    }
                }
            }
        }
        return valid;
    }

    public boolean isMoreThan(Number number, Number numberToCompare) {
        return number.doubleValue() - numberToCompare.doubleValue() > 0;
    }

    public boolean isLessThan(Number number, Number numberToCompare) {
        return number.doubleValue() - numberToCompare.doubleValue() < 0;
    }

    public boolean isEqual(Number number, Number numberToCompare) {
        return number.longValue() == numberToCompare.longValue();
    }

    public boolean isFloatingPointEqual(Double number, Double numberToCompare) {
        return Math.abs(number - numberToCompare) < TOLERATED_FLOATING_POINT_EQUALITY_DIFFERENCE;
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
