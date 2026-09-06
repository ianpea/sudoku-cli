package org.sudoku.app


import org.sudoku.common.status.GameStatus
import org.sudoku.common.status.ShowMoveStatus
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.move.Move
import org.sudoku.domain.move.MoveType
import org.sudoku.domain.move.exception.NoMoveToUndoException
import org.sudoku.domain.move.exception.NoMovesToShowException

class MoveService {
    private var _moveHistory: MutableList<Move> = mutableListOf()
    val moveHistory: List<Move> get() = _moveHistory
    val moveCount: Int get() = _moveHistory.size

    fun addMove(position: CellPosition, previousValue: Int, newValue: Int, type: MoveType) {
        _moveHistory.add(Move(position, previousValue, newValue, type))
    }

    fun undo(): Move {
        if (_moveHistory.isNotEmpty()) {
            val lastMove = _moveHistory.removeLast()
            return lastMove
        } else {
            throw NoMoveToUndoException()
        }
    }

    fun showLastMove(): GameStatus {
        if (_moveHistory.isNotEmpty()) {
            val lastMove = _moveHistory.last()
            return ShowMoveStatus(lastMove)
        }else{
            throw NoMovesToShowException()
        }
    }
}