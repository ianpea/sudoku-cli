package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException

class NumberExistsInRowException(override val message: String): SudokuException(message) {
}