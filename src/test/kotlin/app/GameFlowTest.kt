package org.sudoku.integration

import org.sudoku.app.GameService
import org.sudoku.app.MoveService
import org.sudoku.cli.input.InsertCommand
import org.sudoku.common.status.CompletedGameStatus
import org.sudoku.common.status.InsertStatus
import org.sudoku.domain.cell.CellPosition
import org.sudoku.fixtures.SudokuFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GameFlowTest {
    @Test
    fun `player can complete game by inserting final correct value`() {
        val board = SudokuFixtures.hintableBoard()

        val boardGenerator = FakePuzzleGenerator(board)

        val moveService = MoveService()
        val gameService = GameService(
            moveService = moveService,
            puzzleGenerator = boardGenerator
        )

        gameService.startGame()

        val insertStatus = gameService.insert(
            InsertCommand(
                position = CellPosition(0, 0),
                value = 5
            )
        )
        assertIs<InsertStatus>(insertStatus)


        val status = gameService.check()

        assertIs<CompletedGameStatus>(status)
        assertEquals(5, gameService.board.getCellByRowAndCol(0, 0).value)
        assertEquals(1, moveService.moveCount)
    }
}