package org.sudoku.common

open class SudokuException(override val message: String?) : RuntimeException(message) {
}