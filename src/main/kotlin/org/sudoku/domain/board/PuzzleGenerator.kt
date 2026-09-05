package org.sudoku.domain.board

import org.sudoku.domain.board.Board.Companion.MAX_SOLUTION_COUNT
import org.sudoku.domain.board.exception.InvalidBoardException
import org.sudoku.common.SudokuException
import org.sudoku.domain.cell.CellType

class PuzzleGenerator() {
    fun generatePuzzle(expectedClueCount: Int = 30): Board {
        val board = Board()
        fillBoard(board)
        // With a full board use backtrack to generate a 1 unique solution board.
        var currentClueCount = board.cellCount
        for (i in (0 until board.cellCount).shuffled()) {
            if (currentClueCount <= expectedClueCount) {
                break
            }

            val originalValue = board.cells[i].value

            board.cells[i].value = 0
            board.cells[i].type = CellType.FILLABLE
            if (countSolution(board) == 1) {
                currentClueCount--
            } else {
                board.cells[i].value = originalValue
                board.cells[i].type = CellType.PRE_FILLED
            }
        }

        if (currentClueCount != expectedClueCount) {
            throw InvalidBoardException(
                "Could not generate puzzle with exactly $expectedClueCount clues."
            )
        }
        // If it reaches here, a board with unique result has been generated.
        board.solution = board.cells.map { it.copy() }

        return board
    }

    fun fillBoard(board: Board, index: Int = 0): Boolean {
        if (index == board.cellCount) return true
        val cell = board.cells[index]
        for (value in (1..Board.SIZE).shuffled()) {
            try {
                board.check(cell, value)
                board.cells[index].value = value
                board.cells[index].solution = value
                if (fillBoard(board, index + 1)) {
                    return true
                }
                board.cells[index].value = 0
                board.cells[index].solution = 0
            } catch (_: SudokuException) {
                continue
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

        val cell = board.cells[index]
        var solutionCount = 0

        // This cell is already fixed, so continue to the next cell.
        if (board.cells[index].value != 0) {
            return countSolution(board, index + 1)
        }

        // All your multiverse starts here:
        // each valid value creates a different possible branch.
        for (value in (1..Board.SIZE).shuffled()) {
            try {
                board.check(cell, value)
                board.cells[index].value = value

                val resultFromChild = countSolution(board, index + 1, limit - solutionCount)
                solutionCount += resultFromChild
            } catch (_: SudokuException) {
                // retry
            }

            // Undo this choice before trying another branch.
            board.cells[index].value = 0

            // Stop once this call has found enough solutions
            // to satisfy the requested limit.
            if (solutionCount >= limit) {
                return solutionCount
            }
        }
        return solutionCount
    }
}