package org.sudoku

import org.sudoku.app.MoveService
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.move.MoveType
import org.sudoku.domain.move.exception.NoMoveToUndoException
import org.sudoku.domain.move.exception.NoMovesToShowException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MoveServiceTest {
    private val position = CellPosition(1, 2)

    @Test
    fun `move count depends on current moveHIstory count`(){

        val service = MoveService()

        service.addMove(position, 0, 5, MoveType.INSERT)
        service.addMove(position, 0, 4, MoveType.INSERT)

        assertEquals(2, service.moveCount)
    }

    @Test
    fun `add move records history and increments count`() {
        val service = MoveService()

        service.addMove(position, 0, 5, MoveType.INSERT)

        assertEquals(1, service.moveCount)
        assertEquals(1, service.moveHistory.size)
        assertEquals(5, service.moveHistory.single().newValue)
    }

    @Test
    fun `undoing an insert returns the previous value`() {
        val service = MoveService()
        service.addMove(position, 0, 5, MoveType.INSERT)
        assertEquals(position, service.moveHistory.single().position)
        val move = service.undo()

        assertEquals(0, move.previousValue)
        assertEquals(0, service.moveHistory.size)
    }

    @Test
    fun `undoing a clear returns the previous value`() {
        val service = MoveService()
        service.addMove(position, 5, 0, MoveType.CLEAR)
        assertEquals(position, service.moveHistory.single().position)

        val move = service.undo()

        assertEquals(5, move.previousValue)
        assertEquals(0, service.moveHistory.size)
    }

    @Test
    fun `empty history cannot be undone or displayed`() {
        val service = MoveService()
        service.moveHistory.clear()

        assertFailsWith<NoMoveToUndoException> { service.undo() }
        assertFailsWith<NoMovesToShowException> { service.showLastMove() }
    }
}