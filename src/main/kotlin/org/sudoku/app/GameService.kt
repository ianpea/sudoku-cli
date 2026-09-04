package org.sudoku.app

import org.sudoku.common.result.HintResult
import org.sudoku.common.result.InsertResult
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.PuzzleGenerator
import org.sudoku.domain.board.exception.InvalidBoardException
import org.sudoku.domain.cell.Cell

class GameService {
    val puzzleGenerator = PuzzleGenerator()
    lateinit var board: Board

    fun startGame(): Board {
        board = generateBoard(puzzleGenerator)
        return board
    }

    fun generateBoard(puzzleGenerator: PuzzleGenerator): Board {
        while (true) {
            try {
                return puzzleGenerator.generatePuzzle(80)
            } catch (_: InvalidBoardException) {
                // Retry puzzle generation
            }
        }
    }

    fun check(): Boolean {
        return board.checkWinCondition()
    }

    fun clear() {
        board.clearWholeBoard()
    }

    fun hint(): HintResult {
        val hintResult = board.hint()
        return hintResult
    }

    fun insert(cell: Cell, value: Int): InsertResult {
        return board.insert(cell, value, true)
    }
}