package org.sudoku.app


import org.sudoku.common.status.GameStatus
import org.sudoku.common.status.ShowMoveStatus
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.move.Move
import org.sudoku.domain.move.MoveType
import org.sudoku.domain.move.exception.NoMoveToUndoException
import org.sudoku.domain.move.exception.NoMovesToShowException

class MoveService {
    var moveHistory: MutableList<Move> = mutableListOf()
    val moveCount: Int get() = moveHistory.size

    fun addMove(position: CellPosition, previousValue: Int, newValue: Int, type: MoveType) {
        moveHistory.add(Move(position, previousValue, newValue, type))
    }

    fun undo(): Move {
        if (moveHistory.isNotEmpty()) {
            val lastMove = moveHistory.removeLast()
            return lastMove
        } else {
            throw NoMoveToUndoException()
        }
    }

    fun showLastMove(): GameStatus {
        if (moveHistory.isNotEmpty()) {
            val lastMove = moveHistory.last()
            return ShowMoveStatus(lastMove)
        }else{
            throw NoMovesToShowException()
        }
    }
}