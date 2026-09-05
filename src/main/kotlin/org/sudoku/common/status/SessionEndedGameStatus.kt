package org.sudoku.common.status

data class SessionEndedGameStatus(override val message: String = "Session ended.") : GameStatus
