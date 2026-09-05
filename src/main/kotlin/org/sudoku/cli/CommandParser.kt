package org.sudoku.cli

import org.sudoku.cli.exception.InvalidInputException
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition

class CommandParser(val maxRowAlphabet: Char) {
    fun parse(rawInput: String): GameCommand {
        val input = rawInput.trim().lowercase()

        return when {
            input == "quit" || input == "exit" -> {
                ExitCommand
            }

            input == "check" -> {
                CheckCommand
            }

            input.contains("clear") -> {
                parseClearCommand(input)
            }

            input.contains("hint") -> {
                HintCommand
            }

            input.contains("undo") -> {
                UndoCommand
            }

            input.contains("lastmove") ->{
                ShowLastMoveCommand
            }

            else -> {
                parseInsertCommand(input)
            }
        }
    }

    private fun parseInsertCommand(input: String): InsertCommand {
        val cell = parseInput(input)
        return InsertCommand(
            CellPosition(
                cell.position.row, cell.position.col
                        - 1
            ), value = cell.value
        )
    }

    private fun parseClearCommand(input: String): ClearCommand {
        val cell = parseInput(input)
        return ClearCommand(CellPosition(cell.position.row, cell.position.col - 1))
    }

    private fun parseInput(input: String): Cell {
        val parts = input.split(" ")
        if (parts.size != 2) {
            throw InvalidInputException("Invalid input '$input'.")
        }

        // A5 5 - insert command
        // A5 clear - clear command
        val (part1Raw, part2Raw) = parts
        val rowChar = part1Raw[0].uppercaseChar()
        val row = rowChar - 'A'
        val col = part1Raw[1].digitToIntOrNull()

        val part2 = part2Raw.toIntOrNull()

        if (col == null || col <= 0 || rowChar !in 'A'..maxRowAlphabet) {
            throw InvalidInputException("Invalid input '$input'.")
        }

        // insert command
        if (part2 != null) {
            if (part2 in 1..9) {
                return Cell(CellPosition(row = row, col = col), value = part2)
            }
            throw InvalidInputException("Invalid input '$input'.")
        } else {
            // clear command
            return Cell(CellPosition(row = row, col = col))
        }
    }
}
