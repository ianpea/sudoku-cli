package org.sudoku.domain.cell.exception

import org.sudoku.domain.board.exception.SudokuException
import org.sudoku.domain.cell.Cell

class CannotInsertPreFilledCellException(cell: Cell): SudokuException("Cannot insert to pre-filled cell. ${cell.position.toCoordinateString()}") {

}