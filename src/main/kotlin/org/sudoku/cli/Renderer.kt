package org.sudoku.cli

import org.sudoku.cli.input.GameIO
import org.sudoku.common.status.GameStatus
import org.sudoku.domain.board.Board
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellType

class Renderer(val board: Board, val io: GameIO) {
    fun render(gameStatus: GameStatus?) {
        printColumnHeader(board)
        printHorizontalBorder(board)

        for (cell in board.cells) {
            printCell(board, cell)
        }

        io.println()
        io.println(gameStatus?.message ?: "Enter input (e.g., A3 4, C5 clear, hint, check, undo, lastmove):\n")
        io.println()
    }

    fun renderStatusOnly(gameStatus: GameStatus) {
        io.println(gameStatus.message)
    }

    fun renderWelcomeMessage() {
        io.println(
            "\nWelcome to Sudoku! This puzzle has exactly one solution. Can you solve it? \n\n" +
                    "commands:\n" +
                    "Insert          => 'B1 4'\n" +
                    "Clear           => 'A3 clear'\n" +
                    "Check           => 'check'\n" +
                    "Show Last Move  => 'lastmove'\n" +
                    "Exit            => 'exit' / 'quit\n\n" +
                    "Here is your puzzle:"
        )
    }

    private fun printColumnHeader(board: Board) {
        io.print("  |")

        for (col in 1..Board.SIZE) {
            io.print(col)

            if (col % board.boxSize == 0) {
                io.print("|")
            } else {
                io.print(" ")
            }
        }

        io.println()
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
            io.println()

            if ((row + 1) % board.boxSize == 0) {
                printHorizontalBorder(board)
            }
        }
    }

    private fun printRowHeader(row: Int) {
        io.print('A' + row)
        io.print(" |")
    }

    private fun printCellValue(cell: Cell) {
        if (cell.type == CellType.FILLABLE && cell.value == 0) {
            io.print("_")
        } else {
            io.print(cell.value)
        }
    }

    private fun printCellSeparator(board: Board, col: Int) {
        if ((col + 1) % board.boxSize == 0) {
            io.print("|")
        } else {
            io.print(" ")
        }
    }

    private fun printHorizontalBorder(board: Board) {
        io.print("--+")

        repeat(Board.SIZE) { col ->
            io.print("-")

            if ((col + 1) % board.boxSize == 0) {
                io.print("+")
            } else {
                io.print("-")
            }
        }

        io.println()
    }
}