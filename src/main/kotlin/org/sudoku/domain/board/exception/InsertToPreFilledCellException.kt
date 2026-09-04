package org.sudoku.domain.board.exception

import org.sudoku.domain.cell.Cell

class InsertToPreFilledCellException(cell: Cell): RuntimeException("Cannot insert to pre-filled cell. ${cell.position}") {

}