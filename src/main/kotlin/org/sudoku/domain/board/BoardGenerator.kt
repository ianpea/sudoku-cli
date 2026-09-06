package org.sudoku.domain.board

interface BoardGenerator {
    fun generatePuzzle(expectedClueCount: Int): Board
}