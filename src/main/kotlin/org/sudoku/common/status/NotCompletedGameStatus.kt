package org.sudoku.common.status

data class NotCompletedGameStatus(override val message: String = "Sudoku board still have empty cells."): GameStatus