package org.magofrays.logicalformulas.types;

public enum Connective {
    OR("∨"),
    AND("∧"),
    IMPLIES("→");

    final String value;

    Connective(String value) {
        this.value = value;
    }
}
