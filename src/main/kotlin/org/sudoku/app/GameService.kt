package org.sudoku.app

import org.sudoku.cli.input.ClearCommand
import org.sudoku.cli.input.InsertCommand
import org.sudoku.common.status.CellClearedGameStatus
import org.sudoku.common.status.CompletedGameStatus
import org.sudoku.common.status.GameStatus
import org.sudoku.common.status.NoViolationsStatus
import org.sudoku.common.status.NotCompletedGameStatus
import org.sudoku.common.status.UndoSuccessStatus
import org.sudoku.common.status.ViolationStatus
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.BoardGenerator
import org.sudoku.domain.board.exception.PuzzleGenFailedException
import org.sudoku.domain.move.MoveType
import org.sudoku.domain.violation.ViolationTracker

class GameService(
    private val moveService: MoveService,
    private val puzzleGenerator: BoardGenerator,
    private val violationTracker: ViolationTracker,
    val expectedClueCount: Int = 30
) {
    init {
        require(expectedClueCount in Board.MIN_CLUE_COUNT..Board.CELL_COUNT) {
            "Expected clue count must be between ${Board.MIN_CLUE_COUNT} and ${Board.CELL_COUNT}."
        }
    }

    lateinit var board: Board
        private set
    var hintsUsed: Int = 0
        private set

    fun startGame() {
        board = generateBoard()
    }

    fun generateBoard(): Board {
        repeat(Board.MAX_GENERATION_TRIES) {
            try {
                return puzzleGenerator.generatePuzzle(expectedClueCount)
            } catch (_: PuzzleGenFailedException) {
                // Retry puzzle generation
            }
        }

        throw PuzzleGenFailedException("Could not generate puzzle after ${Board.MAX_GENERATION_TRIES} tries.")
    }

    fun insert(command: InsertCommand): GameStatus {
        val position = command.position
        val cell = board.getCellByRowAndCol(position.row, position.col)
        val previousValue = cell.value

        val status = board.insert(command.position, command.value)
        moveService.addMove(command.position, previousValue, command.value, MoveType.INSERT)
        return status
    }

    fun clear(command: ClearCommand): GameStatus {
        val position = command.position
        val cell = board.getCellByRowAndCol(position.row, position.col)
        val previousValue = cell.value

        board.clear(cell.position)
        moveService.addMove(cell.position, previousValue, 0, MoveType.CLEAR)
        return CellClearedGameStatus(command.position)
    }

    fun checkWinCondition(): GameStatus {
        val win = board.isBoardFull() && check() is NoViolationsStatus
        return if (win) CompletedGameStatus(moveService.moveCount, hintsUsed) else NotCompletedGameStatus()
    }

    fun check(): GameStatus {
        val violation = violationTracker.findViolation(
            board.getRows(),
            board.getColumns(),
            board.getSubgrids()
        )

        return if (violation == null) {
            NoViolationsStatus()
        } else {
            ViolationStatus(violation)
        }
    }

    fun hint(): GameStatus {
        val status = board.hint()
        hintsUsed++
        return status
    }

    fun undo(): GameStatus {
        val move = moveService.undo()
        board.restore(move.position, move.previousValue)
        return UndoSuccessStatus(move)
    }
}