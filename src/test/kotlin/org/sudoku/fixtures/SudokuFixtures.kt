package org.sudoku.fixtures

import org.sudoku.domain.board.Board
import org.sudoku.domain.cell.CellType

object SudokuFixtures {
    fun solvedBoard(): Board {
        val values = listOf(
            5, 3, 4, 6, 7, 8, 9, 1, 2,
            6, 7, 2, 1, 9, 5, 3, 4, 8,
            1, 9, 8, 3, 4, 2, 5, 6, 7,
            8, 5, 9, 7, 6, 1, 4, 2, 3,
            4, 2, 6, 8, 5, 3, 7, 9, 1,
            7, 1, 3, 9, 2, 4, 8, 5, 6,
            9, 6, 1, 5, 3, 7, 2, 8, 4,
            2, 8, 7, 4, 1, 9, 6, 3, 5,
            3, 4, 5, 2, 8, 6, 1, 7, 9
        )

        val board = Board()

        board.cells.forEachIndexed { index, cell ->
            cell.value = values[index]
            cell.type = CellType.PRE_FILLED
        }

        return board
    }

    fun hintableBoard(): Board {
        // expected A1 is hint, cause 0 = not filled yet
        val values = listOf(
            0, 3, 4, 6, 7, 8, 9, 1, 2,
            6, 7, 2, 1, 9, 5, 3, 4, 8,
            1, 9, 8, 3, 4, 2, 5, 6, 7,
            8, 5, 9, 7, 6, 1, 4, 2, 3,
            4, 2, 6, 8, 5, 3, 7, 9, 1,
            7, 1, 3, 9, 2, 4, 8, 5, 6,
            9, 6, 1, 5, 3, 7, 2, 8, 4,
            2, 8, 7, 4, 1, 9, 6, 3, 5,
            3, 4, 5, 2, 8, 6, 1, 7, 9
        )

        val board = Board()

        board.cells.forEachIndexed { index, cell ->
            cell.value = values[index]
            cell.type = CellType.PRE_FILLED
            cell.solution = 5
        }
        board.cells[0].type = CellType.FILLABLE

        return board
    }

    fun noViolationBoard(): Board {
        val values = listOf(
            5, 0, 4, 6, 7, 8, 9, 1, 2,
            6, 7, 2, 1, 9, 5, 3, 4, 8,
            1, 9, 8, 3, 4, 2, 5, 6, 7,
            8, 5, 9, 7, 6, 1, 4, 2, 3,
            4, 2, 6, 8, 5, 3, 7, 9, 1,
            7, 1, 3, 9, 2, 4, 8, 5, 6,
            9, 6, 1, 5, 3, 7, 2, 8, 4,
            2, 8, 7, 4, 1, 9, 6, 3, 5,
            3, 4, 5, 2, 8, 6, 1, 7, 9
        )

        val board = Board()

        board.cells.forEachIndexed { index, cell ->
            cell.value = values[index]
            cell.type = CellType.PRE_FILLED
        }
        board.cells[1].type = CellType.FILLABLE

        return board
    }

    fun violatedBoard(): Board {
        val values = listOf(
            5, 5, 4, 6, 7, 8, 9, 1, 2,
            6, 7, 2, 1, 9, 5, 3, 4, 8,
            1, 9, 8, 3, 4, 2, 5, 6, 7,
            8, 5, 9, 7, 6, 1, 4, 2, 3,
            4, 2, 6, 8, 5, 3, 7, 9, 1,
            7, 1, 3, 9, 2, 4, 8, 5, 6,
            9, 6, 1, 5, 3, 7, 2, 8, 4,
            2, 8, 7, 4, 1, 9, 6, 3, 5,
            3, 4, 5, 2, 8, 6, 1, 7, 9
        )

        val board = Board()

        board.cells.forEachIndexed { index, cell ->
            cell.value = values[index]
            cell.type = CellType.PRE_FILLED
        }
        board.cells[0].type = CellType.FILLABLE
        board.cells[1].type = CellType.FILLABLE

        return board
    }


    fun solvableBoard(): Board {
        val values = listOf(
            0, 3, 4, 6, 7, 8, 9, 1, 2,
            6, 7, 2, 1, 9, 5, 3, 4, 8,
            1, 9, 8, 3, 4, 2, 5, 6, 7,
            8, 5, 9, 7, 6, 1, 4, 2, 3,
            4, 2, 6, 8, 5, 3, 7, 9, 1,
            7, 1, 3, 9, 2, 4, 8, 5, 6,
            9, 6, 1, 5, 3, 7, 2, 8, 4,
            2, 8, 7, 4, 1, 9, 6, 3, 5,
            3, 4, 5, 2, 8, 6, 1, 7, 9
        )

        val board = Board()

        board.cells.forEachIndexed { index, cell ->
            cell.value = values[index]
            cell.type = CellType.PRE_FILLED
        }
        board.cells[0].type = CellType.FILLABLE

        return board
    }}