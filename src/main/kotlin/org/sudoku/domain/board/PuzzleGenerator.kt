package org.sudoku.domain.board

import org.sudoku.domain.board.Board.Companion.MAX_SOLUTION_COUNT
import org.sudoku.domain.board.exception.PuzzleGenFailedException
import org.sudoku.domain.cell.CellType

class PuzzleGenerator() : BoardGenerator {
    override fun generatePuzzle(expectedClueCount: Int): Board {
        val board = Board()
        generateFullyValidBoard(board)
        // With a full board use backtrack to generate a 1 unique solution board.
        var currentClueCount = board.cellCount
        for (i in (0 until board.cellCount).shuffled()) {
            if (currentClueCount <= expectedClueCount) {
                break
            }

            val originalValue = board.getCell(i).value

            board.removeClue(board.getPosition(i))

            if (countSolution(board) == 1) {
                currentClueCount--
            } else {
                board.getCell(i).value = originalValue
                board.getCell(i).type = CellType.PRE_FILLED
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
        if (index == board.cellCount) return true
        val cell = board.getCell(index)
        for (value in (1..Board.SIZE).shuffled()) {
            if (board.canPlaceValue(cell.position, value)) {
                board.getCell(index).value = value
                board.getCell(index).solution = value
                if (generateFullyValidBoard(board, index + 1)) {
                    return true
                }
                board.getCell(index).value = 0
                board.getCell(index).solution = 0
            }
        }

        return false
    }


    fun countSolution(board: Board, index: Int = 0, limit: Int = MAX_SOLUTION_COUNT): Int {
        // Reached past the last cell:
        // this branch produced one complete valid solution.
        if (index == board.cellCount) {
            return 1
        }

        val cell = board.getCell(index)
        var solutionCount = 0

        // This cell is already fixed, so continue to the next cell.
        if (cell.value != 0) {
            return countSolution(board, index + 1)
        }

        // Each valid candidate creates a separate solution branch.
        for (value in (1..Board.SIZE).shuffled()) {
            if (board.canPlaceValue(cell.position, value)) {
                board.getCell(index).value = value

                val resultFromChild = countSolution(board, index + 1, limit - solutionCount)
                solutionCount += resultFromChild
            } else {
                continue
            }

            // Undo this choice before trying another branch.
            board.getCell(index).value = 0

            // Stop once this call has found enough solutions
            // to satisfy the requested limit.
            if (solutionCount >= limit) {
                return solutionCount
            }
        }
        return solutionCount
    }
}