package it.unibz.enums;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.Getter;

@Getter
public enum ValidatorType {
    NUMBER("number"),
    STRING("string"),
    BOOLEAN("boolean"),
    DATE("date"),
    ARRAY("array"),
    OBJECT("object");

    private final String validatorKey;

    ValidatorType(String validatorKey) {
        this.validatorKey = validatorKey;
    }

    public static String of(JsonNodeType nodeType) {
        return switch (nodeType) {
            case ARRAY -> ARRAY.validatorKey;
            case BOOLEAN -> BOOLEAN.validatorKey;
            case NUMBER -> NUMBER.validatorKey;
            case OBJECT -> OBJECT.validatorKey;
            case STRING -> STRING.validatorKey;
            case NULL -> null;
            default -> String.format("Validator type %s unrecognized", nodeType);
        };
    }
}
