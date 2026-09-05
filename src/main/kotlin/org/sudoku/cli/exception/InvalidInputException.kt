package org.sudoku.cli.exception

import org.sudoku.common.SudokuException

class InvalidInputException(override val message: String): ParserException(message) {
}

open class ParserException(override val message: String): SudokuException(message) {
}