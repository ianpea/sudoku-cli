package org.sudoku

import org.sudoku.cli.Renderer
import org.sudoku.common.status.SessionEndedGameStatus
import org.sudoku.domain.board.Board
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RendererTest {
    @Test
    fun `render prints the board and status message`() {
        val output = captureStdout {
            Renderer(Board()).render(SessionEndedGameStatus())
        }

        assertContains(output, "  |1 2 3|4 5 6|7 8 9|")
        assertContains(output, "A |0 0 0|0 0 0|0 0 0|")
        assertContains(output, "Session ended.")
    }

    @Test
    fun `render status only prints the status message`() {
        val output = captureStdout {
            Renderer(Board()).renderStatusOnly(SessionEndedGameStatus())
        }

        assertEquals("Session ended.\n", output)
    }

    private fun captureStdout(block: () -> Unit): String {
        val originalOut = System.out
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        return try {
            block()
            output.toString()
        } finally {
            System.setOut(originalOut)
        }
    }
}