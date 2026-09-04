package org.sudoku.cli

import org.sudoku.cli.status.Status
import org.sudoku.domain.board.Board
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellType

class Renderer {
    fun render(board: Board, status: Status?) {
        printColumnHeader(board)
        printHorizontalBorder(board)

        for (cell in board.cells) {
            printCell(board, cell)
        }

        println()
        println(status?.message ?: "...")
        println()
    }

    fun render(status: Status){
        println(status.message)
    }

    fun renderWelcomeMessage(board: Board): String {
        return "Welcome to Sudoku!\n" +
                "RULES:\n" +
                "#1 Numbers accepted in the cell 1(inclusive) to ${board.size}(inclusive)\n\n" +
                "COMMANDS:\n" +
                "To insert: Specify cell position, i.e. 'A3' followed by a space ' ' and then the value '3' -> 'A3 3'\n" +
                "To check: Type 'check' and enter\n" +
                "To quit: Type 'exit' / 'quit\n\n" +
                "Here is your puzzle:"
    }

    private fun printColumnHeader(board: Board) {
        print("  |")

        for (col in 1..board.size) {
            print(col)

            if (col % board.boxSize == 0) {
                print("|")
            } else {
                print(" ")
            }
        }

        println()
    }

    private fun printCell(board: Board, cell: Cell) {
        val row = cell.position.row
        val col = cell.position.col

        if (col == 0) {
            printRowHeader(row)
        }

        printCellValue(cell)
        printCellSeparator(board, col)

        if (col == board.size - 1) {
            println()

            if ((row + 1) % board.boxSize == 0) {
                printHorizontalBorder(board)
            }
        }
    }

    private fun printRowHeader(row: Int) {
        print('A' + row)
        print(" |")
    }

    private fun printCellValue(cell: Cell) {
        if (cell.type == CellType.FILLABLE && cell.value == 0) {
            print("_")
        } else {
            print(cell.value)
        }
    }

    private fun printCellSeparator(board: Board, col: Int) {
        if ((col + 1) % board.boxSize == 0) {
            print("|")
        } else {
            print(" ")
        }
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