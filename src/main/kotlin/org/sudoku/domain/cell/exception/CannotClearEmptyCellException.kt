package org.sudoku.domain.cell.exception

import org.sudoku.common.SudokuException
import org.sudoku.domain.cell.CellPosition

class CannotClearEmptyCellException(position: CellPosition) :
    SudokuException("Cannot clear empty cell ${position.toCoordinateString()}.") {}
