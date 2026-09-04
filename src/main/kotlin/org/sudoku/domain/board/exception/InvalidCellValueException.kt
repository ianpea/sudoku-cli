package org.sudoku.domain.board.exception

import org.sudoku.domain.cell.Cell

class InvalidCellValueException(cell: Cell, value: Int) :
    RuntimeException("Invalid cell value $value for ${'A' + cell.position.row}${cell.position.col + 1}.") {

}