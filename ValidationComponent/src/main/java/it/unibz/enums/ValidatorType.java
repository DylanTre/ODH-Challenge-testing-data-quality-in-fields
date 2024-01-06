package it.unibz.enums;

import lombok.Getter;

/**
 * Enumeration representing different types of validators.
 */
@Getter
public enum ValidatorType {

    /**
     * Validator type for numbers.
     */
    NUMBER("number"),

    /**
     * Validator type for strings.
     */
    STRING("string"),

    /**
     * Validator type for booleans.
     */
    BOOLEAN("boolean"),

    /**
     * Validator type for dates.
     */
    DATE("date"),

    /**
     * Validator type for arrays.
     */
    ARRAY("array"),

    /**
     * Validator type for objects.
     */
    OBJECT("object");

    private final String validatorKey;

    ValidatorType(String validatorKey) {
        this.validatorKey = validatorKey;
    }
}
