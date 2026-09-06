package org.sudoku.common.status

import org.sudoku.domain.move.Move

data class ShowMoveStatus(val move: Move) : GameStatus {
    override val message: String = "Last move is $move."
}