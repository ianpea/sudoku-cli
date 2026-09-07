package org.sudoku.domain.board

import org.sudoku.domain.board.Board.Companion.MAX_SOLUTION_COUNT
import org.sudoku.domain.board.exception.PuzzleGenFailedException

class PuzzleGenerator() : BoardGenerator {
    override fun generatePuzzle(expectedClueCount: Int): Board {
        val board = Board()
        generateFullyValidBoard(board)
        // With a full board use backtrack to generate a 1 unique solution board.
        var currentClueCount = Board.CELL_COUNT
        for (i in (0 until Board.CELL_COUNT).shuffled()) {
            if (currentClueCount <= expectedClueCount) {
                break
            }

            val position = board.getPosition(i)
            val originalValue = board.removeClue(position)

            if (countSolution(board) == 1) {
                currentClueCount--
            } else {
                board.restoreClue(position, originalValue)
            }
        }

        if (currentClueCount != expectedClueCount) {
            throw PuzzleGenFailedException(
                "Could not generate puzzle with exactly $expectedClueCount clues."
            )
        }
        // If it reaches here, a board with unique result has been generated.
        return board
    }

    fun generateFullyValidBoard(board: Board, index: Int = 0): Boolean {
        if (index == Board.CELL_COUNT) return true
        val position = board.getPosition(index)
        for (value in (1..Board.SIZE).shuffled()) {
            if (board.canPlaceValue(position, value)) {
                board.setValue(index, value)
                board.setSolution(index, value)
                if (generateFullyValidBoard(board, index + 1)) {
                    return true
                }
                board.setValue(index, 0)
                board.setSolution(index, 0)
            }
        }

        return false
    }


    fun countSolution(board: Board, index: Int = 0, limit: Int = MAX_SOLUTION_COUNT): Int {
        // Reached past the last cell:
        // this branch produced one complete valid solution.
        if (index == Board.CELL_COUNT) {
            return 1
        }

        val position = board.getPosition(index)
        var solutionCount = 0

        // This cell is already fixed, so continue to the next cell.
        if (board.getValue(index) != 0) {
            return countSolution(board, index + 1)
        }

        // Each valid candidate creates a separate solution branch.
        for (value in (1..Board.SIZE).shuffled()) {
            if (board.canPlaceValue(position, value)) {
                board.setValue(index, value)

                val resultFromChild = countSolution(board, index + 1, limit - solutionCount)
                solutionCount += resultFromChild

                board.setValue(index, 0)

                if (solutionCount >= limit) {
                    return solutionCount
                }
            }
        }
        return solutionCount
    }
}