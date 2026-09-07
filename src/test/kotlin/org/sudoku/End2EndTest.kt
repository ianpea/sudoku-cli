package org.sudoku

import org.sudoku.domain.board.PuzzleGenerator
import org.sudoku.integration.FakeGameIO
import kotlin.test.Test
import kotlin.test.assertTrue

class End2EndTest {
    @Test
    fun `player can insert check and quit game`() {
        val io = FakeGameIO(
            listOf(
                "A3 4", "check", "quit"
            )
        )

        runGame(
            io = io, puzzleGenerator = PuzzleGenerator()
        )

        // Below asserts are based on the fact that the board is dynamically generated,
        // and the insert command might generate one of the outcomes.
        assertTrue(
            io.output().contains("Inserted 4 to A3.")
                    || io.output().contains("Cannot insert to pre-filled cell A3.")
                    || io.output().contains("already exists in Row")
                    || io.output().contains("already exists in Column")
                    || io.output().contains("already exists in the same 3x3 subgrid")
        )

        assertTrue(
            io.output().contains("No rule violations detected")
                    || io.output().contains("already exists in Row")
                    || io.output().contains("already exists in Column")
                    || io.output().contains("already exists in the same 3x3 subgrid")
        )


        assertTrue(io.output().contains("Game session ended"))
    }
}