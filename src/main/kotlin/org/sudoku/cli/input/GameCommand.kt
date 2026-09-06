package org.sudoku.cli.input

sealed interface GameCommand

data object HintCommand : GameCommand
data object CheckCommand : GameCommand
data object ExitCommand : GameCommand
data object UndoCommand : GameCommand
data object ShowLastMoveCommand : GameCommand
data object ShowViolationsCommand : GameCommand