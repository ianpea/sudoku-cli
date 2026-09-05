package org.sudoku.common.status

import org.sudoku.domain.cell.CellPosition

data class CellClearedGameStatus(
    val position: CellPosition,
    override val message: String = "${position.toCoordinateString()} cleared."
) : GameStatus