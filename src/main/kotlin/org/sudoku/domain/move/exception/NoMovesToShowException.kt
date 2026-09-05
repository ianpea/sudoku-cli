package org.sudoku.domain.move.exception

import org.sudoku.common.SudokuException

data class NoMovesToShowException(override val message: String = "No move to show.") : SudokuException(message) {
}