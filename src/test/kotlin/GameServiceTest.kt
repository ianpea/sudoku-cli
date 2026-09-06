package org.sudoku

import org.junit.jupiter.api.Nested
import org.sudoku.app.GameService
import org.sudoku.app.MoveService
import org.sudoku.cli.input.ClearCommand
import org.sudoku.cli.input.InsertCommand
import org.sudoku.common.status.CompletedGameStatus
import org.sudoku.common.status.NotCompletedGameStatus
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.exception.NoHintLeftException
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import org.sudoku.domain.cell.exception.CannotClearEmptyCellException
import org.sudoku.domain.cell.exception.CannotClearPreFilledCellException
import org.sudoku.domain.cell.exception.CannotInsertPreFilledCellException
import org.sudoku.domain.move.MoveType
import org.sudoku.fixtures.SudokuFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameServiceTest {

    @Nested
    inner class Insert {
        @Test
        fun `inserting to a cell records a move`() {
            val moveService = MoveService()
            val gameService = GameService(moveService, expectedClueCount = 81)
            val position = CellPosition(0, 0)
            gameService.board = Board().also { it.getCellByRowAndCol(0, 0).type = CellType.FILLABLE }

            gameService.insert(InsertCommand(position, 5))

            assertEquals(5, gameService.board.getCellByRowAndCol(0, 0).value)
            assertEquals(1, moveService.moveHistory.size)
        }


        @Test
        fun `inserting to a pre-filled cell throws CannotInsertPreFillCellException`() {
            val moveService = MoveService()
            val gameService = GameService(moveService, expectedClueCount = 81)
            val position = CellPosition(0, 0)
            gameService.board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.PRE_FILLED
                it.getCellByRowAndCol(0, 0).value = 0
            }

            assertFailsWith<CannotInsertPreFilledCellException> {
                gameService.insert(InsertCommand(position, 1))
            }
        }
    }

    @Nested
    inner class Undo {
        @Test
        fun `undoing an insert removes a move and restores the previous value`() {
            val moveService = MoveService()
            val gameService = GameService(moveService, expectedClueCount = 81)
            val position = CellPosition(0, 0)
            gameService.board =
                Board().also { it.getCellByRowAndCol(position.row, position.col).type = CellType.FILLABLE }
            gameService.insert(InsertCommand(position, 5))
            assertEquals(5, gameService.board.getCellByRowAndCol(0, 0).value)
            assertEquals(1, moveService.moveHistory.size)
            assertEquals(MoveType.INSERT, moveService.moveHistory.single().type)

            gameService.undo()

            assertEquals(0, gameService.board.getCellByRowAndCol(0, 0).value)
            assertEquals(0, moveService.moveHistory.size)
        }

        @Test
        fun `undoing a clear removes a move and restores the previous value`() {
            val moveService = MoveService()
            val gameService = GameService(moveService, expectedClueCount = 81)
            val position = CellPosition(0, 0)
            gameService.board =
                Board().also {
                    it.getCellByRowAndCol(position.row, position.col).type = CellType.FILLABLE
                    it.getCellByRowAndCol(position.row, position.col).value = 5
                }
            gameService.clear(ClearCommand(position))
            assertEquals(0, gameService.board.getCellByRowAndCol(0, 0).value)
            assertEquals(1, moveService.moveHistory.size)
            assertEquals(MoveType.CLEAR, moveService.moveHistory.single().type)
            gameService.undo()

            assertEquals(5, gameService.board.getCellByRowAndCol(0, 0).value)
            assertEquals(0, moveService.moveHistory.size)
        }
    }

    @Nested
    inner class Clear {
        @Test
        fun `clearing a filled cell makes it zero`() {
            val moveService = MoveService()
            val gameService = GameService(moveService, expectedClueCount = 81)
            gameService.board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.FILLABLE
                it.getCellByRowAndCol(0, 0).value = 5
            }

            gameService.clear(ClearCommand(CellPosition(0, 0)))

            assertEquals(0, gameService.board.getCellByRowAndCol(0, 0).value)
        }

        @Test
        fun `clearing an empty cell throws CannotClearEmptyCellException`() {
            val moveService = MoveService()
            val gameService = GameService(moveService, expectedClueCount = 81)
            val position = CellPosition(0, 0)
            gameService.board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.FILLABLE
                it.getCellByRowAndCol(0, 0).value = 0
            }

            assertFailsWith<CannotClearEmptyCellException> {
                gameService.clear(ClearCommand(position))
            }
        }

        @Test
        fun `clearing a pre-fill cell throws CannotClearPreFillCellException`() {
            val moveService = MoveService()
            val gameService = GameService(moveService, expectedClueCount = 81)
            val position = CellPosition(0, 0)
            gameService.board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.PRE_FILLED
                it.getCellByRowAndCol(0, 0).value = 0
            }

            assertFailsWith<CannotClearPreFilledCellException> {
                gameService.clear(ClearCommand(position))
            }
        }
    }

    @Nested
    inner class Hint {
        @Test
        fun `returns a valid hint when given unfinished board`() {
            val board = SudokuFixtures.hintableBoard()
            val hintStatus = board.hint()
            assertEquals("Hint: 'A1 5'.", hintStatus.message)
        }

        @Test
        fun `return no hints when a board is completed`() {
            val board = SudokuFixtures.solvedBoard()
            assertFailsWith<NoHintLeftException> {
                board.hint()
            }
        }
    }

    @Nested
    inner class Check {
        @Test
        fun `check should end the game when board is completed`() {
            val board = SudokuFixtures.solvedBoard()
            val status = board.checkWinStatus(1, 1)
            assertEquals(CompletedGameStatus(1, 1), status)
        }

        @Test
        fun `check should not end the game when board is not completed`() {
            val board = SudokuFixtures.hintableBoard()
            val status = board.checkWinStatus(1, 1)
            assertEquals(NotCompletedGameStatus(), status)
        }
    }

    @Test
    fun `game service requires at least 17 clues`() {
        assertFailsWith<IllegalArgumentException> {
            GameService(MoveService(), expectedClueCount = 16)
        }
    }

    @Test
    fun `game requires at most 81 clues`() {
        assertFailsWith<IllegalArgumentException> {
            GameService(MoveService(), 82)
        }
    }

}