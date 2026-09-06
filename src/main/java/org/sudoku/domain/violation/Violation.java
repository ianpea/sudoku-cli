package org.sudoku.domain.violation;

public class Violation {
    private final ViolationType type;
    private final int index;
    private final int value;

    public Violation(
            ViolationType type,
            int index,
            int value
    ) {
        this.type = type;
        this.index = index;
        this.value = value;
    }

    public ViolationType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }
}