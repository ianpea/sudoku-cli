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
    val moveService: MoveService,
    val puzzleGenerator: BoardGenerator,
    val violationTracker: ViolationTracker,
    val expectedClueCount: Int = 30
) {
    init {
        require(expectedClueCount in 23..Board.CELL_COUNT) {
            "Expected clue count must be between 23 and ${Board.CELL_COUNT}. " +
                    "The minimum is limited to 23 to keep puzzle generation time practical."
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
        while (true) {
            try {
                return puzzleGenerator.generatePuzzle(expectedClueCount)
            } catch (_: PuzzleGenFailedException) {
                // Retry puzzle generation
            }
        }
    }

    fun insert(command: InsertCommand): GameStatus {
        val originalCell = board.getCellByRowAndCol(command.position.row, command.position.col).copy()
        val status = board.insert(command.position, command.value)
        moveService.addMove(originalCell.position, originalCell.value, command.value, MoveType.INSERT)
        return status
    }

    fun clear(command: ClearCommand): GameStatus {
        val position = command.position
        val originalCell = board.getCellByRowAndCol(position.row, position.col)
        val originalCellCopy = originalCell.copy()

        board.clear(originalCell.position)

        moveService.addMove(originalCell.position, originalCellCopy.value, 0, MoveType.CLEAR)
        return CellClearedGameStatus(command.position)
    }

    fun checkWinCondition(): GameStatus {
        val win = board.isFullBoard() && check() is NoViolationsStatus
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