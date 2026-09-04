package org.sudoku.domain.board.exception

class ValueExistsInRowException(override val message: String): SudokuException(message) {
}