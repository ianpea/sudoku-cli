package org.sudoku.exceptions.board

import org.sudoku.Cell

class InsertToPreFilledCellException(cell: Cell, value: Int): RuntimeException("Cannot insert to pre-filled cell. ${cell.position}") {

}