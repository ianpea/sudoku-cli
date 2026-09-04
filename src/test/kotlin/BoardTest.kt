package org.sudoku

import org.sudoku.domain.board.Board
import kotlin.test.Test
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
        val cellsWithZeros = board.cells.filter({it.value == 0})
        assertEquals(81, cellsWithZeros.size)
    }
}