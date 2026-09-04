package org.example

import kotlin.math.sqrt


fun main() {
    val size = 9
    val board = Board(size)
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
        board.print()
        val input = readln().trim()

        if (input == "quit") {
            println("Session ended.")
            break
        } else if (input == "check") {
            if (board.checkWinCondition()){
                println("You won!")
                println("Moves used: ")
                break
            }
            println("Not completed")
            continue
        }else if(input == "clear"){
            board.clearWholeBoard()
            continue
        }else if (input == "hint"){
            println(board.hint())
            continue
        }
        val parts = input.split(" ")
        if (parts.size != 2) {
            println("Invalid input '$input'.")
            continue
        }

        val (position, inputValue) = parts
        val row = position[0].uppercaseChar() - 'A'
        val col = position[1].digitToIntOrNull()

        val cellValue = inputValue.toIntOrNull()
        if (col == null || col <= 0) {
            println("Invalid position input '$position'.")
            continue
        }

        if (cellValue == null ||  cellValue !in 1..9) {
            println("Invalid value '$cellValue'.")
            continue
        }

        board.insert(row, col - 1, cellValue, true)
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