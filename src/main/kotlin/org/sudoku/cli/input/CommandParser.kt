package org.sudoku.cli.input

import org.sudoku.cli.exception.InvalidInputException
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition

class CommandParser(val maxRowAlphabet: Char) {
    fun parse(rawInput: String): GameCommand {
        val input = rawInput.trim().uppercase()

        return when {
            input == "QUIT" || input == "EXIT" -> {
                ExitCommand
            }

            input == "CHECK" -> {
                CheckCommand
            }

            input.contains("CLEAR") -> {
                parseClearCommand(input)
            }

            input =="HINT" -> {
                HintCommand
            }

            input == "UNDO" -> {
                UndoCommand
            }

            input == "LASTMOVE" ->{
                ShowLastMoveCommand
            }

            input == "VIOLATIONS" ->{
                ShowViolationsCommand
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

        // A5 5 - insert input
        // A5 clear - clear input
        val (part1Raw, part2Raw) = parts
        val rowChar = part1Raw[0].uppercaseChar()
        val row = rowChar - 'A'
        if(part1Raw.length != 2){
            throw InvalidInputException("Invalid input '$input'.")
        }
        val col = part1Raw[1].digitToIntOrNull()

        val part2 = part2Raw.toIntOrNull()

        if (col == null || col <= 0 || rowChar !in 'A'..maxRowAlphabet) {
            throw InvalidInputException("Invalid input '$input'.")
        }

        // insert input
        if (part2 != null) {
            if (part2 in 1..9) {
                return Cell(CellPosition(row = row, col = col), value = part2)
            }
            throw InvalidInputException("Invalid input '$input'.")
        } else {
            // clear input
            if(part2Raw == "CLEAR"){
                return Cell(CellPosition(row = row, col = col))
            }
            throw InvalidInputException("Invalid input '$input'.")
        }
    }
}
