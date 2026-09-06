package org.sudoku

import org.sudoku.common.SudokuException
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CellTest {
    @Test
    fun `position formats coordinates for users`() {
        val position = CellPosition(2, 4)

        assertEquals("C5", position.toCoordinateString())
        assertEquals('C', position.toUserRow())
        assertEquals(5, position.toUserCol())
    }

    @Test
    fun `fillable cell can be inserted and cleared`() {
        val cell = Cell(CellPosition(0, 0), CellType.FILLABLE)

        cell.insert(7)
        assertEquals(7, cell.value)
        cell.clear()
        assertEquals(0, cell.value)

    }

    @Test
    fun `empty fillable cell cannot be cleared`() {
        val cell = Cell(CellPosition(0, 0), CellType.FILLABLE)

        assertFailsWith<SudokuException> { cell.clear() }
    }

    @Test
    fun `pre-filled cell cannot be inserted or cleared`() {
        val cell = Cell(CellPosition(0, 0))

        assertFailsWith<SudokuException> { cell.insert(7) }
        assertFailsWith<SudokuException> { cell.clear() }
    }
}