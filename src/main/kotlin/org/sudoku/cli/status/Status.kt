package org.sudoku.cli.status

sealed interface Status {
    val message: String
}

data class SessionEndedStatus(override val message: String) : Status

data class CompletedStatus(val moves: Int) : Status {
    override val message: String = "You won!\n" +
            "Moves used: $moves."
}

data class NotCompletedStatus(override val message: String = "Not completed.") : Status

data class InvalidInputStatus(val input: String, override val message: String = "Invalid input '$input'.") : Status

data class InvalidPositionStatus(
    val position: String,
    override val message: String = "Invalid position input '$position'."
) : Status