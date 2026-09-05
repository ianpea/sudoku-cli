package org.sudoku.common.status

import org.sudoku.domain.cell.Cell

sealed interface Status {
    val message: String
}

data class SessionEndedStatus(override val message: String = "Session ended.") : Status

data class CellClearedStatus(
    val cell: Cell,
    override val message: String = "${cell.position.toCoordinateString()} cleared."
) : Status

data class CompletedStatus(val moves: Int) : Status {
    override val message: String = "You won!\n" +
            "Moves used: $moves."
}
