package org.sudoku.common.status

import org.sudoku.domain.cell.Cell

data class InsertStatus(
    val cell: Cell
) : GameStatus {
    override val message: String = "Inserted ${cell.value} to ${'A' + cell.position.row}${cell.position.col+1}."
}
