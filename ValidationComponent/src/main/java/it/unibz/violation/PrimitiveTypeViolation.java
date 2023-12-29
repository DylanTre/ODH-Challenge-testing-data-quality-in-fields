package it.unibz.violation;

import lombok.Getter;

import java.util.List;

@Getter
public class PrimitiveTypeViolation extends Violation {
    private List<String> violations;
}
