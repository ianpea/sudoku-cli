package org.sudoku.domain.move.exception

import org.sudoku.domain.board.exception.SudokuException

data class NoMoveToUndoException(override val message: String = "No move to undo!") : SudokuException(message) {
}