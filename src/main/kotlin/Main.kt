package org.sudoku

import org.sudoku.exceptions.NoHintLeftException
import org.sudoku.exceptions.board.InsertToPreFilledCellException
import org.sudoku.exceptions.board.InvalidCellValueException
import kotlin.math.sqrt


fun main() {
    val size = 9
    val board = Board(size)
    val renderer = Renderer()

    var cachedOutput = ""
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
    while (true) {
        try {
            board.generatePuzzle(80)
            break // success, exit loop
        } catch (e: IllegalStateException) {
            // failed, try again
        }
    }

    println("Here is your puzzle:")

    while (true) {
        renderer.render(board, cachedOutput)
        cachedOutput = ""
        val input = readln().trim()

        if (input == "quit") {
            cachedOutput = "Session ended."
//            println(cachedOutput)
            break
        } else if (input == "check") {
            if (board.checkWinCondition()) {
                // TODO
                cachedOutput = "You won!" +
                        "Moves used: "
//                println("You won!")
//                println("Moves used: ")
                break
            }
            cachedOutput = "Not Completed"
//            println("Not completed")
            continue
        } else if (input == "clear") {
            board.clearWholeBoard()
            continue
        } else if (input == "hint") {
            try {
                cachedOutput = board.hint()
            } catch (e: NoHintLeftException) {

                //TODO
                cachedOutput = e.message ?: ""
            } finally {
                continue
            }
        }
        val parts = input.split(" ")
        if (parts.size != 2) {
            cachedOutput = "Invalid input '$input'."
            continue
        }

        val (position, inputValue) = parts
        val rowChar = position[0].uppercaseChar()
        val col = position[1].digitToIntOrNull()

        val cellValue = inputValue.toIntOrNull()
        if (col == null || col <= 0 || rowChar !in 'A'..board.MAX_ROW) {
            cachedOutput = ("Invalid position input '$position'.")
            continue
        }

        val row = rowChar - 'A'

        if (cellValue == null || cellValue !in 1..9) {
            cachedOutput = ("Invalid value '$cellValue'.")
            continue
        }
        try {
            board.insert(row, col - 1, cellValue, true)
        } catch (e: InsertToPreFilledCellException) {
            cachedOutput = e.message ?: ""
        } catch (e: InvalidCellValueException) {
            cachedOutput = e.message ?: ""
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