package org.sudoku.app

import org.sudoku.fixtures.SudokuFixtures
import org.sudoku.integration.FakeGameIO
import org.sudoku.integration.FakePuzzleGenerator
import org.sudoku.runGame
import kotlin.test.Test
import kotlin.test.assertContains

class GameFlowTest {
    @Test
    fun `player can complete game by inserting final correct value`() {
        val board = SudokuFixtures.hintableBoard()

        val io = FakeGameIO(listOf("A1 5", "A1 2", "undo", "a1 5", "check"))
        runGame(io, FakePuzzleGenerator(board))

        val output = io.output()
        assertContains(output, "You won!")
    }

    @Test
    fun `player can not complete the game with unfinished board`() {
        val board = SudokuFixtures.hintableBoard()

        val io = FakeGameIO(listOf("A1 4", "check", "quit"))
        runGame(io, FakePuzzleGenerator(board))

        val output = io.output()
        assertContains(output, "not completed")
    }
}