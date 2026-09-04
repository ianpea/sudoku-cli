package org.sudoku.domain.board.exception

class InvalidBoardException(override val message: String): SudokuException(message) {
}