package org.sudoku

class Renderer {
    fun render(board: Board, cachedOutput: String) {
        print("  |")
        for (i in 1..board.size) {
            print("$i")
            if (i % board.boxSize == 0) print("|") else print(" ")
        }
        println()
        printHorizontalBorder(board)
        for (i in 0 until board.cellCount) {
            val cell = board.cells[i]

            val row = cell.position.row
            val col = cell.position.col
            if (col == 0) {
                print('A' + cell.position.row)
                print(" |")
            }

            if (cell.type == CellType.FILLABLE && cell.value == 0) {
                print("_")
            } else {
                print("${cell.value}")
            }

            if((col+1) % board.boxSize == 0 && col != 0) print("|") else print(" ")

            // Row border
            if (col == board.size - 1) {
                println()
                if ((row + 1) % board.boxSize == 0) {
                    printHorizontalBorder(board)
                }
            }
        }
        println()

        println(cachedOutput)
        println()
    }

    private fun printHorizontalBorder(board: Board) {
        print("--+")
        repeat(board.size) { col ->
            print("-")
            if ((col + 1) % board.boxSize == 0) {
                print("+")
            } else {
                print("-")
            }
        }
        println()
    }
}