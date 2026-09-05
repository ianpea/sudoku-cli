package org.sudoku.domain.cell.exception

import org.sudoku.domain.board.exception.SudokuException

class CannotClearPreFilledCellException(override val message: String = "Cannot clear pre-filled cell."): SudokuException(message) {
}