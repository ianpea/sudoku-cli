package org.sudoku.common.status

import org.sudoku.domain.violation.Violation

class ShowViolationsGameStatus(private val violations: List<Violation>) : GameStatus {
    override val message: String
        get() = if (violations.isEmpty()) {
            "No violations recorded."
        } else {
            violations.joinToString("\n") { it.message() }
        }
}