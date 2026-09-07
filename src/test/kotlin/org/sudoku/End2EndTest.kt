package org.sudoku

import org.sudoku.fixtures.SudokuFixtures
import org.sudoku.integration.FakeGameIO
import org.sudoku.integration.FakePuzzleGenerator
import kotlin.test.Test
import kotlin.test.assertTrue

class End2EndTest {
    @Test
    fun `player can insert and win the game`() {
        val io = FakeGameIO(
            listOf(
                "A1 5", "I9 9", "check", "quit"
            )
        )

        val board = SudokuFixtures.solvableBoard()
        runGame(
            io = io, puzzleGenerator = FakePuzzleGenerator(board)
        )

        assertTrue(io.output().contains("Inserted 5 to A1."))
        assertTrue(io.output().contains("Cannot insert to pre-filled cell I9."))
        assertTrue(
            io.output().contains("You won!")
        )
    }

    @Test
    fun `player can insert, undo and quit the game`() {
        val io = FakeGameIO(
            listOf(
                "A1 4", "undo", "check", "quit"
            )
        )

        val board = SudokuFixtures.solvableBoard()
        runGame(
            io = io, puzzleGenerator = FakePuzzleGenerator(board)
        )

        // Below asserts are based on the fact that the board is dynamically generated,
        // and the insert command might generate one of the outcomes.
        assertTrue(io.output().contains("Inserted 4 to A1."))
        assertTrue { io.output().contains("Undo successful") }
        assertTrue { io.output().contains("No rule violations detected.") }

        assertTrue(
            io.output().contains("Game session ended")
        )
    }

    @Test
    fun `player can view violations`() {
        val io = FakeGameIO(
            listOf(
                "A1 4", "check", "quit"
            )
        )

        val board = SudokuFixtures.solvableBoard()
        runGame(
            io = io, puzzleGenerator = FakePuzzleGenerator(board)
        )

        // Below asserts are based on the fact that the board is dynamically generated,
        // and the insert command might generate one of the outcomes.
        assertTrue(io.output().contains("Inserted 4 to A1."))
        assertTrue { io.output().contains("already exists in") }

        assertTrue(
            io.output().contains("Game session ended")
        )
    }
}