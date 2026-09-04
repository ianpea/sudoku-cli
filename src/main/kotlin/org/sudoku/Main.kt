package org.sudoku

import org.sudoku.cli.Renderer
import org.sudoku.cli.status.CompletedStatus
import org.sudoku.cli.status.DomainStatus
import org.sudoku.cli.status.ErrorStatus
import org.sudoku.cli.status.InvalidInputStatus
import org.sudoku.cli.status.InvalidPositionStatus
import org.sudoku.cli.status.NotCompletedStatus
import org.sudoku.cli.status.SessionEndedStatus
import org.sudoku.cli.status.Status
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.exception.NoHintLeftException
import org.sudoku.domain.board.exception.SudokuException
import kotlin.math.sqrt


fun main() {
    val size = 9
    val board = Board(size)
    val renderer = Renderer()

    var statusMessage: Status? = null
    println(
        "Welcome to Sudoku!\n" +
                "RULES:\n" +
                "#1 Numbers accepted in the cell 1(inclusive) to ${board.size}(inclusive)\n" +
                "\n" +
                "COMMANDS:\n" +
                "To insert: Specify cell position, i.e. 'A3' followed by a space ' ' and then the value '3' -> 'A3 3'\n" +
                "To check: Type 'check' and enter\n" +
                "To quit: Type 'exit' / 'quit\n"
    )

    if (!isValidSize(size)) {
        println("Invalid size '$size'")
        return
    }
    board.fillBoard()

    var puzzleGenerated = false
    while (!puzzleGenerated) {
        try {
            board.generatePuzzle(80)
            puzzleGenerated = true
        } catch (_: IllegalStateException) {
            // Retry puzzle generation
        }
    }

    println("Here is your puzzle:")

    while (true) {
        renderer.render(board, statusMessage)
        statusMessage = null
        val input = readln().trim()

        if (input == "quit" || input == "exit") {
            statusMessage = SessionEndedStatus("Session ended.")
            renderer.render(statusMessage)
            break
        } else if (input == "check") {
            if (board.checkWinCondition()) {
                // TODO
                statusMessage = CompletedStatus(1)
            } else {
                statusMessage = NotCompletedStatus()
            }
            continue
        } else if (input == "clear") {
            board.clearWholeBoard()
            continue
        } else if (input == "hint") {
            try {
                statusMessage = DomainStatus(board.hint())
            } catch (e: NoHintLeftException) {

                //TODO
                statusMessage = ErrorStatus(e.message ?: "An unexpected error has occurred.")
            } finally {
                continue
            }
        }
        val parts = input.split(" ")
        if (parts.size != 2) {
            statusMessage = InvalidInputStatus(input)
            continue
        }

        val (position, inputValue) = parts
        val rowChar = position[0].uppercaseChar()
        val col = position[1].digitToIntOrNull()

        val cellValue = inputValue.toIntOrNull()
        if (col == null || col <= 0 || rowChar !in 'A'..board.MAX_ROW) {
            statusMessage = InvalidPositionStatus(position)
            continue
        }

        val row = rowChar - 'A'

        if (cellValue == null || cellValue !in 1..9) {
            statusMessage = ErrorStatus("Invalid value '$cellValue'.")
            continue
        }
        try {
            val insertResult = board.insert(row, col - 1, cellValue, true)
            statusMessage = DomainStatus(insertResult)
        } catch (e: SudokuException) {
            statusMessage = ErrorStatus(e.message)
        }
    }
}

fun isValidSize(boardLen: Int): Boolean {
    val givenBoardSize = boardLen * boardLen
    val givenLen = sqrt(givenBoardSize.toDouble()).toInt()
    if (givenLen * givenLen != givenBoardSize) {
        return false
    }
    return true
}