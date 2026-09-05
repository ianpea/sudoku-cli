package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException

class ValueExistsInBoxException(override val message: String): SudokuException(message) {
}