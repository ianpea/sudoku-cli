package org.sudoku.app

import org.junit.jupiter.api.Nested
import org.sudoku.cli.input.ClearCommand
import org.sudoku.cli.input.InsertCommand
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.exception.NoHintLeftException
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import org.sudoku.domain.cell.exception.CannotClearEmptyCellException
import org.sudoku.domain.cell.exception.CannotClearPreFilledCellException
import org.sudoku.domain.cell.exception.CannotInsertPreFilledCellException
import org.sudoku.domain.move.MoveType
import org.sudoku.domain.violation.ViolationTracker
import org.sudoku.fixtures.SudokuFixtures
import org.sudoku.integration.FakeGameIO
import org.sudoku.integration.FakePuzzleGenerator
import org.sudoku.runGame
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameServiceTest {

    val moveService = MoveService()

    @Nested
    inner class Insert {
        @Test
        fun `inserting to a cell records a move`() {
            val board = Board().also { it.getCellByRowAndCol(0, 0).type = CellType.FILLABLE }
            val gameService =
                GameService(moveService, FakePuzzleGenerator(board), ViolationTracker(), expectedClueCount = 81)
            gameService.startGame()
            val position = CellPosition(0, 0)

            gameService.insert(InsertCommand(position, 5))

            assertEquals(5, gameService.board.getCellByRowAndCol(0, 0).value)
            assertEquals(1, moveService.moveHistory.size)
        }


        @Test
        fun `inserting to a pre-filled cell throws CannotInsertPreFillCellException`() {
            val board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.PRE_FILLED
                it.getCellByRowAndCol(0, 0).value = 0
            }
            val gameService =
                GameService(moveService, FakePuzzleGenerator(board), ViolationTracker(), expectedClueCount = 81)
            gameService.startGame()
            val position = CellPosition(0, 0)

            assertFailsWith<CannotInsertPreFilledCellException> {
                gameService.insert(InsertCommand(position, 1))
            }
        }
    }

    @Nested
    inner class Undo {
        @Test
        fun `undoing an insert removes a move and restores the previous value`() {
            val position = CellPosition(0, 0)
            val board =
                Board().also { it.getCellByRowAndCol(position.row, position.col).type = CellType.FILLABLE }
            val gameService =
                GameService(moveService, FakePuzzleGenerator(board), ViolationTracker(), expectedClueCount = 81)
            gameService.startGame()
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
            val position = CellPosition(0, 0)
            val board = Board().also {
                it.getCellByRowAndCol(position.row, position.col).type = CellType.FILLABLE
                it.getCellByRowAndCol(position.row, position.col).value = 5
            }
            val gameService =
                GameService(moveService, FakePuzzleGenerator(board), ViolationTracker(), expectedClueCount = 81)
            gameService.startGame()
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
            val board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.FILLABLE
                it.getCellByRowAndCol(0, 0).value = 5
            }
            val gameService =
                GameService(moveService, FakePuzzleGenerator(board), ViolationTracker(), expectedClueCount = 81)
            gameService.startGame()

            gameService.clear(ClearCommand(CellPosition(0, 0)))

            assertEquals(0, gameService.board.getCellByRowAndCol(0, 0).value)
        }

        @Test
        fun `clearing an empty cell throws CannotClearEmptyCellException`() {
            val moveService = MoveService()
            val position = CellPosition(0, 0)
            val board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.FILLABLE
                it.getCellByRowAndCol(0, 0).value = 0
            }
            val gameService =
                GameService(moveService, FakePuzzleGenerator(board), ViolationTracker(), expectedClueCount = 81)
            gameService.startGame()

            assertFailsWith<CannotClearEmptyCellException> {
                gameService.clear(ClearCommand(position))
            }
        }

        @Test
        fun `clearing a pre-fill cell throws CannotClearPreFillCellException`() {
            val moveService = MoveService()
            val position = CellPosition(0, 0)
            val board = Board().also {
                it.getCellByRowAndCol(0, 0).type = CellType.PRE_FILLED
                it.getCellByRowAndCol(0, 0).value = 0
            }
            val gameService =
                GameService(moveService, FakePuzzleGenerator(board), ViolationTracker(), expectedClueCount = 81)
            gameService.startGame()

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
        fun `check returns no violations when no violation found`() {
            val board = SudokuFixtures.noViolationBoard()
            val puzzleGenerator = FakePuzzleGenerator(board)
            val io = FakeGameIO(listOf("check", "quit"))
            runGame(io, puzzleGenerator)
            assertContains(io.output(), "No violations found.")
        }

        @Test
        fun `row check returns violations when violation found`() {
            val board = SudokuFixtures.rowViolatedBoard()
            val puzzleGenerator = FakePuzzleGenerator(board)
            val io = FakeGameIO(listOf("check", "quit"))
            runGame(io, puzzleGenerator)
            assertContains(io.output(), "Number 5 already exists in Row A.")
        }

        @Test
        fun `col check returns violations when violation found`() {
            val board = SudokuFixtures.colViolatedBoard()
            val puzzleGenerator = FakePuzzleGenerator(board)
            val io = FakeGameIO(listOf("check", "quit"))
            runGame(io, puzzleGenerator)
            assertContains(io.output(), "Number 5 already exists in Column 1.")
        }

        @Test
        fun `subgrid check returns violations when violation found`() {
            val board = SudokuFixtures.subgridViolatedBoard()
            val puzzleGenerator = FakePuzzleGenerator(board)
            val io = FakeGameIO(listOf("check", "quit"))
            runGame(io, puzzleGenerator)
            assertContains(io.output(), "Number 5 already exists in the same 3x3 subgrid #1.")
        }
    }

    @Nested
    inner class GameFlow {
        @Test
        fun `game ends when board is filled and no violation found`() {
            val board = SudokuFixtures.solvableBoard()
            val puzzleGenerator = FakePuzzleGenerator(board)
            val io = FakeGameIO(listOf("A1 5"))
            runGame(io, puzzleGenerator)
            assertContains(io.output(), "You won!")
        }

        @Test
        fun `game does not end when board is full but contains violation`() {
            val board = SudokuFixtures.fullViolatedBoard()
            val puzzleGenerator = FakePuzzleGenerator(board)
            val io = FakeGameIO(listOf("check", "quit"))
            runGame(io, puzzleGenerator)
            assertContains(io.output(), "Number 4 already exists in Row A.")
        }

        @Test
        fun `game service requires at least 17 clues`() {
            assertFailsWith<IllegalArgumentException> {
                GameService(MoveService(), FakePuzzleGenerator(Board()), ViolationTracker(), expectedClueCount = 16)
            }
        }

        @Test
        fun `game requires at most 81 clues`() {
            assertFailsWith<IllegalArgumentException> {
                GameService(MoveService(), FakePuzzleGenerator(Board()), ViolationTracker(), expectedClueCount = 82)
            }
        }

        @Test
        fun `player can complete game by inserting final correct value`() {
            val board = SudokuFixtures.hintableBoard()

            val io = FakeGameIO(listOf("A1 5", "A1 2", "undo", "a1 5", "check"))
            runGame(io, FakePuzzleGenerator(board))

            val output = io.output()
            assertContains(output, "You won!")
        }
    }

}