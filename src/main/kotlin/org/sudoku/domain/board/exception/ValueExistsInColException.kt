package org.sudoku.domain.board.exception

class ValueExistsInColException(override val message: String): SudokuException(message) {
}