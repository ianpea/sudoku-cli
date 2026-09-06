package org.sudoku.cli.input

import org.sudoku.domain.cell.CellPosition

sealed interface MoveCommand

data class InsertCommand(val position: CellPosition, val value: Int) : MoveCommand, GameCommand
data class ClearCommand(val position: CellPosition) : MoveCommand, GameCommand