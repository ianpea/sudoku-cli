package org.sudoku.common.status

data class NoViolationsStatus(override val message: String = "No violations found.") : GameStatus {

}
