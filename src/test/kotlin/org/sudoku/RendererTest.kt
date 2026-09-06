package org.sudoku

import org.sudoku.cli.Renderer
import org.sudoku.common.status.SessionEndedGameStatus
import org.sudoku.domain.board.Board
import org.sudoku.integration.FakeGameIO
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RendererTest {

    @Test
    fun `render prints the board and status message`() {
        val io = FakeGameIO(listOf(""))
        Renderer(Board(), io).render(SessionEndedGameStatus())

        val output = io.output()

        assertContains(output, "  |1 2 3|4 5 6|7 8 9|")
        assertContains(output, "A |0 0 0|0 0 0|0 0 0|")
        assertContains(output, "Session ended.")
    }

    @Test
    fun `render status only prints the status message`() {
        val io = FakeGameIO(listOf(""))
        Renderer(Board(), io).renderStatusOnly(SessionEndedGameStatus())
        val output = io.output()

        assertEquals("Session ended.\n", output)
    }
}