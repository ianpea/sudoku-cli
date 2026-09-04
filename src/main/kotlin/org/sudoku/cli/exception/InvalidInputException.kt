package org.sudoku.cli.exception

import org.sudoku.cli.ParserException

class InvalidInputException(override val message: String): ParserException(message) {
}