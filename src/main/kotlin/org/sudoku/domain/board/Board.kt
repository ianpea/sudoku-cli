package org.sudoku.domain.board

import org.sudoku.domain.board.exception.InsertToPreFilledCellException
import org.sudoku.domain.board.exception.InvalidCellValueException
import org.sudoku.domain.board.exception.ValueExistsInBoxException
import org.sudoku.domain.board.exception.ValueExistsInColException
import org.sudoku.domain.board.exception.ValueExistsInRowException
import org.sudoku.common.result.HintResult
import org.sudoku.common.result.InsertResult
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import org.sudoku.domain.board.exception.NoHintLeftException

class Board() {
    val maxRowAlphabet = 'A' + (SIZE - 1)
    val cellCount: Int = CELL_COUNT
    val boxSize: Int = BOX_SIZE
    val cells: List<Cell> = List(cellCount) { index ->
        Cell(CellPosition(index / SIZE, index % SIZE))
    }

    lateinit var solution: List<Cell>

    /**
     * Check whether given value can be filled into the cell.
     * @param self When true, checks the cell itself whether its value is truthy in a board or not
     */
    fun check(cell: Cell, value: Int, self: Boolean = false): Boolean {
        val row = cell.position.row
        val col = cell.position.col
        val index = row * SIZE + col

        val hypotheticalBoard = cells.toMutableList()

        val originalValue = hypotheticalBoard[index].value
        if (self) {
            hypotheticalBoard[index].value = 0
        }

        val hypotheticalRow =
            hypotheticalBoard.filter { cell -> cell.position.row == row }.filter { cell -> value != cell.value }
                .map { cell -> cell.value }
        val hypotheticalCol =
            hypotheticalBoard.filter { cell -> cell.position.col == col }.filter { cell -> value != cell.value }
                .map { cell -> cell.value }


        // row check
        if (hypotheticalRow.contains(value)) {
            throw ValueExistsInRowException("Value '$value' already exists in the row: [${hypotheticalRow.joinToString(", ")}].")
        }

        // column check
        if (hypotheticalCol.contains(value)) {
            throw ValueExistsInColException("Value '$value' already exists in the col: [${hypotheticalCol.joinToString(", ")}].")
        }

        // box check
        val hypotheticalBox = getBox(cell.position)
        if (hypotheticalBox.contains(value)) {
            throw ValueExistsInBoxException("Value '$value' already exists in the box: [${hypotheticalBox.joinToString(", ")}].")
        }


        if (self) {
            hypotheticalBoard[index].value = originalValue
        }
        return true
    }

    /**
     *   Insert the value into the cell, if its not pre-filled.
     *   @param smart Flag to determine whether user input is checked before filling in the cell. Defaults to false
     */
    fun insert(cell: Cell, valueToBe: Int, smart: Boolean = false): InsertResult {
        val index = cell.position.row * SIZE + cell.position.col
        val cell = cells[index]

        if (cell.type == CellType.PRE_FILLED) {
            // requirement #1 Invalid move
            throw InsertToPreFilledCellException(cell)
        } else {
            if (smart) {
                if (check(cell, valueToBe)) {
                    cells[index].value = valueToBe
                } else {
                    // value is not valid in the cell
                    throw InvalidCellValueException(cell, valueToBe)
                }
            } else {
                // TODO: blindly insert
                cells[index].value = valueToBe
            }
            return InsertResult(cell)
        }
    }

    fun getBox(position: CellPosition): IntArray {
        val startRow = (position.row / boxSize) * boxSize
        val startCol = (position.col / boxSize) * boxSize

        val boxContents =
            cells.filter { cell ->
                cell.position.row in startRow until startRow + boxSize
                        && cell.position.col in startCol until startCol + boxSize
            }.map { it.value }.toIntArray()

        return boxContents
    }

    fun clearWholeBoard() {
        for (i in 0 until cellCount) {
            if (cells[i].type == CellType.FILLABLE) {
                cells[i].value = 0
            }
        }
    }

    fun checkWinCondition(): Boolean {
        for (i in 0 until cellCount) {
            if (!check(cells[i], cells[i].value, self = true)) {
                return false
            }
        }
        return true
    }

    fun hint(): HintResult {
        val emptyCells = cells.filter({ it.type == CellType.FILLABLE && it.value == 0 })
        val hint = emptyCells.randomOrNull()
        if (hint != null) {
            return HintResult(hint)
        }

        throw NoHintLeftException()
    }

    companion object {
        const val MAX_SOLUTION_COUNT = 2
        const val SIZE = 9
        const val BOX_SIZE = 3
        const val CELL_COUNT = SIZE * SIZE
    }
}
