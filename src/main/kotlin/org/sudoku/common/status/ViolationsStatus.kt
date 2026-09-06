package org.sudoku.common.status

import org.sudoku.domain.violation.Violation
import org.sudoku.domain.violation.ViolationType

data class ViolationStatus(
    val violation: Violation
) : GameStatus {

    override val message: String = when (violation.type) {
        ViolationType.ROW ->
            "Number ${violation.value} already exists in Row ${('A'.code + violation.index).toChar()}."

        ViolationType.COLUMN ->
            "Number ${violation.value} already exists in Column ${violation.index + 1}."

        ViolationType.SUBGRID ->
            "Number ${violation.value} already exists in the same 3x3 subgrid #${violation.index + 1}."
    }
}