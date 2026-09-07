package org.sudoku.domain.board

import org.sudoku.domain.cell.CellPosition

data class Hint(
    val position: CellPosition,
    val value: Int
)