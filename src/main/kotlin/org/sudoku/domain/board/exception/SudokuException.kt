package org.sudoku.domain.board.exception

open class SudokuException(override val message: String?): RuntimeException(message) {
}