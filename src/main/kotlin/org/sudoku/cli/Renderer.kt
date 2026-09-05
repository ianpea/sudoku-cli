package org.sudoku.cli

import org.sudoku.common.status.Status
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
        println(status?.message ?: "Enter command (e.g., A3 4, C5 clear, hint, check):\n")
        println()
    }

    fun render(status: Status){
        println(status.message)
    }

    fun renderWelcomeMessage() {
        println( "\nWelcome to Sudoku! This puzzle has exactly one solution. Can you solve it? \n\n" +
                "commands:\n" +
                "Insert => 'B1 4'\n" +
                "Clear  => 'A3 clear'\n" +
                "Check  => 'check'\n" +
                "Exit   => 'exit' / 'quit\n\n" +
                "Here is your puzzle:")
    }

    private fun printColumnHeader(board: Board) {
        print("  |")

        for (col in 1..Board.SIZE) {
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

        if (col == Board.SIZE - 1) {
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

        repeat(Board.SIZE) { col ->
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