package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException

class ValueExistsInSubGridException(override val message: String): SudokuException(message) {
}