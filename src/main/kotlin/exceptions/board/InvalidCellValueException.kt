package org.sudoku.exceptions.board

import org.sudoku.Cell

class InvalidCellValueException(cell: Cell, value: Int): RuntimeException("Invalid cell value $value.") {

}