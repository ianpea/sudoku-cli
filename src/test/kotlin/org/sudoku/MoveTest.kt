package org.sudoku

import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.move.Move
import org.sudoku.domain.move.MoveType
import kotlin.test.Test
import kotlin.test.assertEquals

class MoveTest {
    @Test
    fun `insert move describes the inserted value`() {
        val move = Move(CellPosition(0, 0), 0, 5, MoveType.INSERT)

        assertEquals("INSERT A1, with value: 5", move.toString())
    }

    @Test
    fun `clear move describes the cleared coordinate`() {
        val move = Move(CellPosition(2, 4), 5, 0, MoveType.CLEAR)

        assertEquals("CLEAR C5", move.toString())
    }
}