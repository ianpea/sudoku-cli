package org.sudoku.common.status

import org.sudoku.domain.move.Move

data class UndoSuccessStatus(val move: Move) : GameStatus {
    override val message: String =
        "Undo successful: ${move.position.toCoordinateString()} restored to ${move.previousValue}."
}
