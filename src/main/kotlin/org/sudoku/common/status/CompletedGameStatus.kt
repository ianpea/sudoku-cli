package org.sudoku.common.status

data class CompletedGameStatus(val moveCount: Int, val hintCount: Int) : GameStatus {
    override val message: String = "You won!\n" +
            "Moves used: $moveCount.\n" +
            "Hints used: $hintCount."
}
