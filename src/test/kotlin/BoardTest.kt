package org.sudoku

import org.sudoku.domain.board.Board
import org.sudoku.domain.board.exception.ValueExistsInSubGridException
import org.sudoku.domain.board.exception.ValueExistsInColException
import org.sudoku.domain.board.exception.ValueExistsInRowException
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals

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
        val board = Board()
        val position = CellPosition(0, 0)
        board.getCellByRowAndCol(0, 0).type = CellType.FILLABLE

        val status = board.insert(position, 4)

        assertEquals(4, board.getCellByRowAndCol(0, 0).value)
        assertEquals("Inserted 4 to A1.", status.message)
    }

    @Test
    fun `insert rejects a pre-filled cell`() {
        val board = Board()

        assertFailsWith<RuntimeException> {
            board.insert(CellPosition(0, 0), 4)
        }
    }

    @Test
    fun `check rejects duplicates in the same row`() {
        val board = Board()
        board.getCellByRowAndCol(0, 1).value = 4

        assertFailsWith<ValueExistsInRowException> {
            board.check(board.getCellByRowAndCol(0, 0), 4)
        }
    }

    @Test
    fun `check rejects duplicates in the same column`() {
        val board = Board()
        board.getCellByRowAndCol(1, 0).value = 4

        assertFailsWith<ValueExistsInColException> {
            board.check(board.getCellByRowAndCol(0, 0), 4)
        }
    }

    @Test
    fun `check rejects duplicates in the same subgrid`() {
        val board = Board()
        board.getCellByRowAndCol(1, 1).value = 4

        assertFailsWith<ValueExistsInSubGridException> {
            board.check(board.getCellByRowAndCol(0, 0), 4)
        }
    }

    @Test
    fun `check win status is incomplete when a fillable cell is empty`() {
        val board = Board()
        board.getCellByRowAndCol(0, 0).type = CellType.FILLABLE

        assertEquals("Sudoku board is not completed, please fill in the empty cell(s).", board.checkWinStatus(0, 0).message)
    }
}