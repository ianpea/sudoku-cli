package org.sudoku.cli

import org.sudoku.cli.exception.InvalidInputException
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition

class CommandParser(val maxRowAlphabet: Char) {
    fun parse(rawInput: String): GameCommand{
        val input = rawInput.trim().lowercase()

        return when(input){
            "quit", "exit" ->{
                ExitCommand
//                statusMessage = SessionEndedStatus("Session ended.")
//                renderer.render(statusMessage)
//                break
            }
            "check"->{
                CheckCommand
//                if (board.checkWinCondition()) {
//                    // TODO
//                    statusMessage = CompletedStatus(1)
//                } else {
//                    statusMessage = NotCompletedStatus()
//                }
//                continue
            }
            "clear" -> {
                CheckCommand
//                board.clearWholeBoard()
//                continue
            }
            "hint" ->{
                HintCommand
//                try {
//                    statusMessage = DomainStatus(board.hint())
//                } catch (e: NoHintLeftException) {
//                    statusMessage = ErrorStatus(e.message)
//                } finally {
//                    continue
//                }
            }
            else -> {
                parseInsertCommand(input)
            }
        }
    }

    private fun parseInsertCommand(input: String): InsertCommand {
        val parts = input.split(" ")
        if (parts.size != 2) {
            throw InvalidInputException("Invalid input '$input'.")
        }

        val (position, inputValue) = parts
        val rowChar = position[0].uppercaseChar()
        val col = position[1].digitToIntOrNull()

        val cellValue = inputValue.toIntOrNull()
        if (col == null || col <= 0 || rowChar !in 'A'..maxRowAlphabet) {
            throw InvalidInputException("Invalid input '$input'.")
//            statusMessage = InvalidPositionStatus(position)
//            continue
        }

        if (cellValue == null || cellValue !in 1..9) {
            throw InvalidInputException("Invalid input '$input'.")
        }

        val row = rowChar - 'A'
        return InsertCommand(Cell(CellPosition(row, col -1)), value = cellValue)
    }
}
