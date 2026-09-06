package org.sudoku

import org.sudoku.domain.board.Board
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.exception.CannotInsertPreFilledCellException
import org.sudoku.fixtures.SudokuFixtures
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BoardTest {
    @Test
    fun `default board contains 81 cells`() {
        val board = Board()

        assertEquals(81, board.cells.size)
    }

    @Test
    fun `default board is filled with 0s`() {
        val board = Board()
        val cellsWithZeros = board.cells.filter { it.value == 0 }
        assertEquals(81, cellsWithZeros.size)
    }

    @Test
    fun `insert fills a fillable cell and returns its status`() {
        val board = SudokuFixtures.solvableBoard()
        val position = CellPosition(0, 0)

        val status = board.insert(position, 5)
        val insertedCell = board.getCellByRowAndCol(position.row, position.col)

        assertEquals(5, insertedCell.value)
        assertEquals("Inserted 5 to A1.", status.message)
    }

    @Test
    fun `insert rejects a pre-filled cell`() {
        val board = Board()

        assertFailsWith<CannotInsertPreFilledCellException> {
            board.insert(CellPosition(0, 0), 4)
        }
    }

    @Test
    fun `check rejects duplicates in the same row`() {
        val board = Board()
        board.getCellByRowAndCol(0, 1).value = 4

        assertFalse {
            board.canPlaceValue(CellPosition(0, 0), 4)
        }
    }

    @Test
    fun `check rejects duplicates in the same column`() {
        val board = Board()
        board.getCellByRowAndCol(0, 0).value = 4

        assertFalse {
            board.canPlaceValue(CellPosition(0, 1), 4)
        }
    }

    @Test
    fun `check rejects duplicates in the same subgrid`() {
        val board = Board()
        board.getCellByRowAndCol(1, 1).value = 4

        assertFalse {
            board.canPlaceValue(CellPosition(0, 0), 4)
        }
        assertFalse {
            board.canPlaceValue(CellPosition(2, 2), 4)
        }
    }
}