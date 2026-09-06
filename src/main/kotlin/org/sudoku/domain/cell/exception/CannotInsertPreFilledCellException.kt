package org.sudoku.domain.cell.exception

import org.sudoku.common.SudokuException
import org.sudoku.domain.cell.CellPosition

class CannotInsertPreFilledCellException(position: CellPosition) :
    SudokuException("Cannot insert to pre-filled cell ${position.toCoordinateString()}.") {

}