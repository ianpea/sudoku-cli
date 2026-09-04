package org.sudoku.domain.board.exception

class ValueExistsInBoxException(override val message: String): SudokuException(message) {
}