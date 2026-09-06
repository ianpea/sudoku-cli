package org.sudoku.domain.violation;

public record Violation(
        ViolationType type,
        int value,
        int row,
        int col
) {
    public String message() {
        String coordinate = (char) ('A' + row) + String.valueOf(col + 1);
        return switch (type) {
            case ROW -> "Number " + value + " already exists in Row " + (char) ('A' + row) + ".";
            case COLUMN -> "Number " + value + " already exists in Col " + (col + 1) + ".";
            case SUBGRID -> "Number " + value + " already exists in the same 3×3 subgrid of " + coordinate + ".";
        };
    }
}

