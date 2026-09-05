package org.sudoku.common.status

data class CompletedGameStatus(val moves: Int) : GameStatus {
    override val message: String = "You won!\n" +
            "Moves used: $moves."
}
