package org.sudoku.domain.cell.exception

import org.sudoku.domain.board.exception.SudokuException
import org.sudoku.domain.cell.CellPosition

class CannotClearEmptyCellException(position: CellPosition) :
    SudokuException("Cannot clear cell ${position.toCoordinateString()} cause its empty.") {}
