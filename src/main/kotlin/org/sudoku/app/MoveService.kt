package org.sudoku.app

import org.sudoku.cli.ClearCommand
import org.sudoku.cli.InsertCommand
import org.sudoku.cli.MoveCommand
import org.sudoku.common.status.GameStatus
import org.sudoku.common.status.ShowMoveStatus
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.move.Move
import org.sudoku.domain.move.MoveType
import org.sudoku.domain.move.exception.NoMoveToUndoException
import org.sudoku.domain.move.exception.NoMovesToShowException

class MoveService {
    var moveHistory: MutableList<Move> = mutableListOf()

    fun addMove(position: CellPosition, previousValue: Int, newValue: Int, type: MoveType) {
        moveHistory.add(Move(position, previousValue, newValue, type))
    }

    fun undo(): Pair<Move, MoveCommand> {
        if (moveHistory.isNotEmpty()) {
            val lastMove = moveHistory.removeLast()

            return when (lastMove.type) {
                MoveType.INSERT -> {
                    Pair(lastMove, ClearCommand(lastMove.position))
                }

                MoveType.CLEAR -> {
                    Pair(lastMove, InsertCommand(lastMove.position, lastMove.previousValue))
                }

            }
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