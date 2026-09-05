package org.sudoku.domain.move

import org.sudoku.domain.cell.CellPosition

data class Move(
    val position: CellPosition,
    val previousValue: Int,
    val newValue: Int,
    val type: MoveType
)

enum class MoveType {
    INSERT,
    CLEAR
}