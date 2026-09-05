package org.sudoku.common.status

data class NotCompletedStatus(override val message: String = "Sudoku board still have empty cells."): Status