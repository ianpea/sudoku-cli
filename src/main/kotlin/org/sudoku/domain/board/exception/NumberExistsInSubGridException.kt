package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException

class NumberExistsInSubGridException(override val message: String): SudokuException(message) {
}