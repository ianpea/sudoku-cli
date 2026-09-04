package org.sudoku.cli.status

sealed interface Status {
    val message: String
}

data class SessionEndedStatus(override val message: String = "Session ended.") : Status

data class BoardClearedStatus(override val message: String = "Board cleared.") : Status

data class CompletedStatus(val moves: Int) : Status {
    override val message: String = "You won!\n" +
            "Moves used: $moves."
}

data class NotCompletedStatus(override val message: String = "Not completed.") : Status
