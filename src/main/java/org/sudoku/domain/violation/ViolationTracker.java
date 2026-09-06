package org.sudoku.domain.violation;

import java.util.List;

public class ViolationTracker {

    public Violation findViolation(
            List<List<Integer>> rows,
            List<List<Integer>> columns,
            List<List<Integer>> boxes
    ) {
        Violation violation = findDuplicate(rows, ViolationType.ROW);

        if (violation != null) {
            return violation;
        }

        violation = findDuplicate(columns, ViolationType.COLUMN);

        if (violation != null) {
            return violation;
        }

        return findDuplicate(boxes, ViolationType.SUBGRID);
    }

    private Violation findDuplicate(
            List<List<Integer>> groups,
            ViolationType type
    ) {
        for (int index = 0; index < groups.size(); index++) {
            boolean[] seen = new boolean[10];

            for (int value : groups.get(index)) {
                if (value == 0) {
                    continue;
                }

                if (seen[value]) {
                    return new Violation(type, index, value);
                }

                seen[value] = true;
            }
        }

        return null;
    }
}