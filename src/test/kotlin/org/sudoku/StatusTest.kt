package org.sudoku

import org.sudoku.common.status.CellClearedGameStatus
import org.sudoku.common.status.CompletedGameStatus
import org.sudoku.common.status.ErrorStatus
import org.sudoku.common.status.HintStatus
import org.sudoku.common.status.InsertStatus
import org.sudoku.common.status.NotCompletedGameStatus
import org.sudoku.common.status.SessionEndedGameStatus
import org.sudoku.common.status.ShowMoveStatus
import org.sudoku.common.status.UndoSuccessStatus
import org.sudoku.domain.board.Hint
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import org.sudoku.domain.move.Move
import org.sudoku.domain.move.MoveType
import kotlin.test.Test
import kotlin.test.assertEquals

class StatusTest {
    private val position = CellPosition(0, 0)

    @Test
    fun `statuses expose user-facing messages`() {
        val cell = Cell(position, CellType.FILLABLE, value = 4, solution = 4)
        val move = Move(position, 0, 4, MoveType.INSERT)

        assertEquals("You won!\nMoves used: 3.\nHints used: 1.", CompletedGameStatus(3, 1).message)
        assertEquals(
            "Sudoku board is not completed, please fill in the empty cell(s).",
            NotCompletedGameStatus().message
        )
        assertEquals("Inserted 4 to A1.", InsertStatus(cell).message)
        assertEquals("A1 cleared.", CellClearedGameStatus(position).message)
        assertEquals("Hint: 'A1 4'.", HintStatus(Hint(cell.position, cell.solution)).message)
        assertEquals("Last move is INSERT A1, with value: 4.", ShowMoveStatus(move).message)
        assertEquals("Undo successful: A1 restored to 0.", UndoSuccessStatus(move).message)
        assertEquals("An unexpected error has occurred.", ErrorStatus(null).message)
        assertEquals("Game session ended.", SessionEndedGameStatus().message)
    }
}