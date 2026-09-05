package org.sudoku.common.status

import org.sudoku.domain.cell.Cell

data class CellClearedGameStatus(
    val cell: Cell,
    override val message: String = "${cell.position.toCoordinateString()} cleared."
) : GameStatus