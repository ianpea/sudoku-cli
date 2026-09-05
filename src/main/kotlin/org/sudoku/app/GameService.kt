package org.sudoku.app

import org.sudoku.cli.ClearCommand
import org.sudoku.cli.InsertCommand
import org.sudoku.common.status.CellClearedGameStatus
import org.sudoku.common.status.DomainStatus
import org.sudoku.common.status.GameStatus
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.PuzzleGenerator
import org.sudoku.domain.board.exception.InvalidBoardException
import org.sudoku.domain.move.Move

class GameService(val expectedClueCount: Int = 30) {

    init {
        require(expectedClueCount >= 17) {
            "\n\n********* \n\nA standard 9x9 Sudoku with a unique solution requires at least 17 clues.\n" + "Proof here => https://arxiv.org/abs/1201.0749 \n\n*********\n"
        }
    }

    val puzzleGenerator = PuzzleGenerator()
    lateinit var board: Board
    var moves: MutableList<Move> = mutableListOf()
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
        return DomainStatus(board.insert(command.cell, command.value))
    }

    fun clear(command: ClearCommand): GameStatus {
        command.cell.clear()
        return CellClearedGameStatus(command.cell)
    }

    fun check(): GameStatus {
        return board.checkWinStatus()
    }

    fun hint(): GameStatus {
        return DomainStatus(board.hint())
    }

    fun move() {

    }
}