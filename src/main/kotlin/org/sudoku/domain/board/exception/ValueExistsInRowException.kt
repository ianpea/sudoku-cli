package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException

class ValueExistsInRowException(override val message: String): SudokuException(message) {
}