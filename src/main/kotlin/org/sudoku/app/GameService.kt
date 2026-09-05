package org.sudoku.app

import org.sudoku.domain.board.Board
import org.sudoku.domain.board.PuzzleGenerator
import org.sudoku.domain.board.exception.InvalidBoardException
import org.sudoku.domain.cell.Cell

class GameService(val expectedClueCount: Int = 30) {

    init {
        require(expectedClueCount >= 17) {
            "\n\n********* \n\nA standard 9x9 Sudoku with a unique solution requires at least 17 clues.\n" + "Proof here => https://arxiv.org/abs/1201.0749 \n\n*********\n"
        }
    }

    val puzzleGenerator = PuzzleGenerator()
    lateinit var board: Board

    fun startGame(): Board {
        board = generateBoard(puzzleGenerator)
        return board
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

    fun clear(cell: Cell) {
        cell.clear()
    }
}