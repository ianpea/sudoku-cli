package org.sudoku.domain.cell.exception

import org.sudoku.domain.board.exception.SudokuException
import org.sudoku.domain.cell.CellPosition

class CannotClearPreFilledCellException(position: CellPosition): SudokuException("Cannot clear pre-filled cell ${position.toCoordinateString()}.") {
}