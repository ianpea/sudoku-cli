package org.sudoku.integration

import org.sudoku.domain.board.Board
import org.sudoku.domain.board.BoardGenerator

class FakePuzzleGenerator(val board: Board) : BoardGenerator {
    override fun generatePuzzle(expectedClueCount: Int): Board {
        return board
    }
}