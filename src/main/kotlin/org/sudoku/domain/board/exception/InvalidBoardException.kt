package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException

class InvalidBoardException(override val message: String): SudokuException(message) {
}