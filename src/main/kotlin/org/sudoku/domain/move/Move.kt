package org.sudoku.domain.move

import org.sudoku.domain.cell.CellPosition

data class Move(
    val position: CellPosition,
    val previousValue: Int,
    val newValue: Int,
    val type: MoveType
) {
    override fun toString(): String {
        return if (type == MoveType.INSERT) {
            "$type ${position.toCoordinateString()}, with value: $newValue"
        } else {
            "$type ${position.toCoordinateString()}"
        }
    }
}

enum class MoveType {
    INSERT,
    CLEAR
}