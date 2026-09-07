package org.sudoku.common.status

data class NoViolationsStatus(override val message: String = "No rule violations detected.") : GameStatus {

}
