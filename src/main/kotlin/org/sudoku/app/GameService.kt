package org.sudoku.app

import org.sudoku.cli.input.ClearCommand
import org.sudoku.cli.input.InsertCommand
import org.sudoku.common.status.CellClearedGameStatus
import org.sudoku.common.status.GameStatus
import org.sudoku.common.status.UndoSuccessStatus
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.PuzzleGenerator
import org.sudoku.domain.board.exception.InvalidBoardException
import org.sudoku.domain.move.MoveType

class GameService(val moveService: MoveService, val expectedClueCount: Int = 30) {

    init {
        require(expectedClueCount >= 17) {
            "\n\n********* \n\nA standard 9x9 Sudoku with a unique solution requires at least 17 clues.\n" + "Proof here => https://arxiv.org/abs/1201.0749 \n\n*********\n"
        }
    }

    val puzzleGenerator = PuzzleGenerator()
    lateinit var board: Board
    var hintsUsed = 0

    fun startGame() {
        board = generateBoard(puzzleGenerator)
    }

    fun generateBoard(puzzleGenerator: PuzzleGenerator): Board {
        while (true) {
            try {
                return puzzleGenerator.generatePuzzle(expectedClueCount)
            } catch (_: InvalidBoardException) {
                // Retry puzzle generation
            }
        }
    }

    fun insert(command: InsertCommand): GameStatus {
        // Get original copy
        val originalCell = board.getCellByRowAndCol(command.position.row, command.position.col).copy()

        // Perform input
        val status = board.insert(command.position, command.value)

        // Add move once input succeeds
        moveService.addMove(originalCell.position, originalCell.value, command.value, MoveType.INSERT)
        return status
    }

    fun clear(command: ClearCommand): GameStatus {
        // Get original copy
        val position = command.position
        val originalCell = board.getCellByRowAndCol(position.row, position.col)
        val originalCellCopy = originalCell.copy()

        // Perform input
        originalCell.clear()

        // Add move once input succeeds
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
        val (move, command) = moveService.undo()
        when (command) {
            is InsertCommand -> board.insert(command.position, command.value)
            is ClearCommand -> {
                val cell = board.getCellByRowAndCol(command.position.row, command.position.col)
                cell.clear()
            }
        }
        return UndoSuccessStatus(move)
    }
}