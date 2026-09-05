package org.sudoku.domain.move.exception

import org.sudoku.common.SudokuException

data class NoMoveToUndoException(override val message: String = "No move to undo.") : SudokuException(message) {
}