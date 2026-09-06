package org.sudoku.app

import org.sudoku.cli.input.ClearCommand
import org.sudoku.cli.input.InsertCommand
import org.sudoku.common.status.CellClearedGameStatus
import org.sudoku.common.status.GameStatus
import org.sudoku.common.status.UndoSuccessStatus
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.BoardGenerator
import org.sudoku.domain.board.exception.PuzzleGenFailedException
import org.sudoku.domain.move.MoveType

class GameService(val moveService: MoveService, val puzzleGenerator: BoardGenerator, val expectedClueCount: Int = 30) {
    init {
        require(expectedClueCount in 17..Board.CELL_COUNT) {
            "\n\n********* \n\nA standard 9x9 Sudoku with a unique solution requires at least 17 clues.\n" + "Proof here => https://arxiv.org/abs/1201.0749 \n\n*********\n"
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

        originalCell.clear()

        moveService.addMove(originalCell.position, originalCellCopy.value, 0, MoveType.CLEAR)
        return CellClearedGameStatus(command.position)
    }

    fun check(): GameStatus {
        return board.checkWinStatus(moveService.moveCount, hintsUsed)
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