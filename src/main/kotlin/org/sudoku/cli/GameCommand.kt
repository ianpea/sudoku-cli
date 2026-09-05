package org.sudoku.cli

import org.sudoku.domain.cell.Cell

sealed interface GameCommand

data class InsertCommand(val cell: Cell, val value: Int): GameCommand
data class ClearCommand(val cell: Cell): GameCommand
data object HintCommand: GameCommand
data object CheckCommand: GameCommand
data object ExitCommand: GameCommand