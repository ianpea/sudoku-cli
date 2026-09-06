package org.sudoku.domain.violation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViolationTracker {
    private final List<Violation> violations = new ArrayList<>();

    public void add(Violation violation) {
        violations.add(violation);
    }

    public List<Violation> getViolations() {
        return Collections.unmodifiableList(violations);
    }

    public void clear() {
        violations.clear();
    }
}