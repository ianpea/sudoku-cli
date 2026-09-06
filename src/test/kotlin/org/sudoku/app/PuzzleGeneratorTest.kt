package org.sudoku.app

import org.sudoku.domain.board.Board
import org.sudoku.domain.board.PuzzleGenerator
import org.sudoku.domain.cell.CellType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PuzzleGeneratorTest {
    @Test
    fun `fillBoard creates a complete valid board`() {
        val board = Board()
        val generator = PuzzleGenerator()

        assertTrue(generator.fillBoard(board))
        assertTrue(board.cells.all { it.value in 1..Board.SIZE })
        assertEquals(1, generator.countSolution(board))
    }

    @Test
    fun `generatePuzzle creates the requested number of clues`() {
        val board = PuzzleGenerator().generatePuzzle(expectedClueCount = 81)

        assertEquals(81, board.cells.count { it.type == CellType.PRE_FILLED })
    }

    @Test
    fun `generated puzzle board should only contain 1 solution`() {
        val puzzleGenerator = PuzzleGenerator()
        val board = puzzleGenerator.generatePuzzle(30)
        val solutionCount = puzzleGenerator.countSolution(board, 0, 2)

        assertEquals(1, solutionCount)
    }
}