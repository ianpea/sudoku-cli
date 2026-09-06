package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException

class PuzzleGenFailedException(override val message: String) : SudokuException(message) {
}