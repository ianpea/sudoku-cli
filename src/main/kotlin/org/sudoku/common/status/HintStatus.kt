package org.sudoku.common.status

import org.sudoku.domain.cell.Cell

data class HintStatus(
    val hint: Cell
) : GameStatus {
    override val message: String = "Hint: '${'A' + (hint.position.row)}${hint.position.col + 1} ${hint.solution}'."
}