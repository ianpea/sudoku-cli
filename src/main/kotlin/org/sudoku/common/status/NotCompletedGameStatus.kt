package org.sudoku.common.status

data class NotCompletedGameStatus(override val message: String = "Sudoku board is not completed, please fill in the empty cell(s).") :
    GameStatus