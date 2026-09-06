package org.sudoku.domain.board

import org.sudoku.common.status.CompletedGameStatus
import org.sudoku.common.status.GameStatus
import org.sudoku.domain.cell.exception.CannotInsertPreFilledCellException
import org.sudoku.domain.board.exception.NumberExistsInSubGridException
import org.sudoku.domain.board.exception.NumberExistsInColException
import org.sudoku.domain.board.exception.NumberExistsInRowException
import org.sudoku.common.status.InsertStatus
import org.sudoku.common.status.HintStatus
import org.sudoku.common.status.NotCompletedGameStatus
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import org.sudoku.domain.board.exception.NoHintLeftException
import org.sudoku.common.SudokuException
import org.sudoku.domain.violation.Violation
import org.sudoku.domain.violation.ViolationType

class Board() {
    val cellCount: Int = CELL_COUNT
    val boxSize: Int = BOX_SIZE
    val cells: List<Cell> = List(cellCount) { index ->
        Cell(CellPosition(index / SIZE, index % SIZE))
    }

    /**
     * Check whether given value can be filled into the cell.
     */
    fun check(cell: Cell, value: Int) {
        val row = cell.position.row
        val col = cell.position.col

        val rowContents =
            cells.filter { cell -> cell.position.row == row && cell.position.col != col }.map { cell -> cell.value }
        val colContents =
            cells.filter { cell -> cell.position.col == col && cell.position.row != row }.map { cell -> cell.value }

        // row
        if (rowContents.contains(value)) {
            throw NumberExistsInRowException(Violation(ViolationType.ROW, value, row, col))
        }

        // column
        if (colContents.contains(value)) {
            throw NumberExistsInColException(Violation(ViolationType.COLUMN, value, row, col))
        }

        // subgrid 3x3
        val subGridContents = getSubGrid(cell.position)
        if (subGridContents.contains(value)) {
            throw NumberExistsInSubGridException(Violation(ViolationType.SUBGRID, value, row, col))
        }
    }

    /**
     *   Insert the value into the cell, if its not pre-filled.
     *   @param smart Flag to determine whether user input is checked before filling in the cell. Defaults to false
     */
    fun insert(position: CellPosition, valueToBe: Int, smart: Boolean = true): InsertStatus {
        val index = position.row * SIZE + position.col
        val cell = cells[index]

        if (cell.type == CellType.PRE_FILLED) {
            // requirement #1 Invalid move
            throw CannotInsertPreFilledCellException(cell.position)
        } else {
            if (smart) {
                check(cell, valueToBe)
            }
            cell.insert(valueToBe)
            return InsertStatus(cell)
        }
    }

    fun getSubGrid(position: CellPosition): IntArray {
        val startRow = (position.row / boxSize) * boxSize
        val startCol = (position.col / boxSize) * boxSize

        val boxContents =
            cells.filter { cell ->
                cell.position.row in startRow until startRow + boxSize
                        && cell.position.col in startCol until startCol + boxSize
                        && !(cell.position.row == position.row && cell.position.col == position.col)
            }.map { it.value }.toIntArray()

        return boxContents
    }

    fun checkWinStatus(moveCount: Int, hintCount: Int): GameStatus {
        try {
            if (cells.none({ it.type == CellType.FILLABLE && it.value == 0 })) {
                for (i in 0 until cellCount) {
                    check(cells[i], cells[i].value)
                }
            } else {
                return NotCompletedGameStatus()
            }
        } catch (e: SudokuException) {
            return NotCompletedGameStatus(e.message ?: "Sudoku not completed yet.")
        }
        return CompletedGameStatus(moveCount, hintCount)
    }

    fun hint(): GameStatus {
        val emptyCells = cells.filter({ it.type == CellType.FILLABLE && it.value == 0 })
        val hint = emptyCells.randomOrNull()
        if (hint != null) {
            return HintStatus(hint)
        }

        throw NoHintLeftException()
    }

    fun getCellByRowAndCol(row: Int, col: Int): Cell {
        return cells[row * SIZE + col]
    }

    fun restore(position: CellPosition,value: Int){
        cells[position.row * SIZE + position.col].value = value
    }

    companion object {
        const val MAX_SOLUTION_COUNT = 2
        const val SIZE = 9
        const val BOX_SIZE = 3
        const val CELL_COUNT = SIZE * SIZE
        const val MAX_ROW_ALPHABET = 'I'

    }
}
