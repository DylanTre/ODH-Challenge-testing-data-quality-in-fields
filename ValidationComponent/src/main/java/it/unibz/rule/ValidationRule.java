package it.unibz.rule;

import lombok.Getter;

@Getter
public abstract class ValidationRule {
    private String name;
    private boolean satisfied;

    abstract boolean ruleSatisfied();
}
