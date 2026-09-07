package org.sudoku.common.status

import org.sudoku.domain.board.Hint

data class HintStatus(
    val hint: Hint
) : GameStatus {
    override val message: String = "Hint: '${'A' + (hint.position.row)}${hint.position.col + 1} ${hint.value}'."
}