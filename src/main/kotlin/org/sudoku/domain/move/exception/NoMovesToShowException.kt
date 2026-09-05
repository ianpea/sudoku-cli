package org.sudoku.domain.move.exception

import org.sudoku.domain.board.exception.SudokuException

data class NoMovesToShowException(override val message: String = "No moves to show!") : SudokuException(message) {
}