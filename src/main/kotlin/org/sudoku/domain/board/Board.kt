package org.sudoku.domain.board

import org.sudoku.common.status.GameStatus
import org.sudoku.domain.cell.exception.CannotInsertPreFilledCellException
import org.sudoku.common.status.InsertStatus
import org.sudoku.common.status.HintStatus
import org.sudoku.domain.cell.Cell
import org.sudoku.domain.cell.CellPosition
import org.sudoku.domain.cell.CellType
import org.sudoku.domain.board.exception.NoHintLeftException

class Board {
    private val _cells: List<Cell> = List(CELL_COUNT) { index ->
        Cell(CellPosition(index / SIZE, index % SIZE))
    }

    val cells: List<Cell> get() = _cells

    fun canPlaceValue(position: CellPosition, value: Int): Boolean {
        val row = position.row
        val col = position.col

        val rowContents =
            _cells.filter { cell -> cell.position.row == row && cell.position.col != col }.map { cell -> cell.value }
        val colContents =
            _cells.filter { cell -> cell.position.col == col && cell.position.row != row }.map { cell -> cell.value }

        // row
        if (rowContents.contains(value)) {
            return false
        }

        // column
        if (colContents.contains(value)) {
            return false
        }

        // subgrid 3x3
        val subGridContents = getSubgrid(position)
        if (subGridContents.contains(value)) {
            return false
        }
        return true
    }

    fun insert(position: CellPosition, valueToBe: Int): InsertStatus {
        val index = position.row * SIZE + position.col
        val cell = _cells[index]

        if (cell.type == CellType.PRE_FILLED) {
            throw CannotInsertPreFilledCellException(cell.position)
        } else {
            cell.insert(valueToBe)
            return InsertStatus(cell)
        }
    }

    fun clear(position: CellPosition) {
        getCellByRowAndCol(position.row, position.col).clear()
    }

    fun isBoardFull(): Boolean =
        _cells.none { it.type == CellType.FILLABLE && it.value == 0 }

    fun hint(): GameStatus {
        val emptyCells = _cells.filter({ it.type == CellType.FILLABLE && it.value == 0 })
        val hint = emptyCells.randomOrNull()
        if (hint != null) {
            return HintStatus(hint)
        }

        throw NoHintLeftException()
    }

    fun getCellByRowAndCol(row: Int, col: Int): Cell {
        return _cells[row * SIZE + col]
    }

    fun restore(position: CellPosition, value: Int) {
        _cells[position.row * SIZE + position.col].value = value
    }

    fun getSubgrid(position: CellPosition): IntArray {
        val startRow = (position.row / BOX_SIZE) * BOX_SIZE
        val startCol = (position.col / BOX_SIZE) * BOX_SIZE

        val boxContents =
            _cells.filter { cell ->
                cell.position.row in startRow until startRow + BOX_SIZE
                        && cell.position.col in startCol until startCol + BOX_SIZE
                        && !(cell.position.row == position.row && cell.position.col == position.col)
            }.map { it.value }.toIntArray()

        return boxContents
    }

    fun getRows(): List<List<Int>> =
        (0 until SIZE).map { row ->
            _cells
                .filter { it.position.row == row }
                .map { it.value }
        }

    fun getColumns(): List<List<Int>> =
        (0 until SIZE).map { col ->
            _cells
                .filter { it.position.col == col }
                .map { it.value }
        }

    fun getSubgrids(): List<List<Int>> =
        (0 until SIZE).map { subgridIndex ->
            val startRow = (subgridIndex / BOX_SIZE) * BOX_SIZE
            val startCol = (subgridIndex % BOX_SIZE) * BOX_SIZE

            _cells
                .filter { cell ->
                    cell.position.row in startRow until startRow + BOX_SIZE &&
                            cell.position.col in startCol until startCol + BOX_SIZE
                }
                .map { it.value }
        }

    fun getCell(index: Int): Cell {
        return _cells[index]
    }

    fun getCell(position: CellPosition): Cell {
        return _cells[position.row * SIZE + position.col]
    }

    fun getPosition(index: Int): CellPosition {
        return getCell(index).position
    }

    fun restoreClue(position: CellPosition, value: Int) {
        val cell = getCell(position)

        cell.value = value
        cell.type = CellType.PRE_FILLED
    }

    fun removeClue(position: CellPosition): Int {
        val cell = getCell(position)
        val previousValue = cell.value

        cell.value = 0
        cell.type = CellType.FILLABLE

        return previousValue
    }

    fun setValue(index: Int, value: Int) {
        getCell(index).value = value
    }

    fun setSolution(index: Int, value: Int) {
        getCell(index).solution = value
    }

    companion object {
        const val MAX_SOLUTION_COUNT = 2
        const val SIZE = 9
        const val BOX_SIZE = 3
        const val CELL_COUNT = SIZE * SIZE
        const val MAX_ROW_ALPHABET = 'I'
        const val MIN_CLUE_COUNT = 30
        const val MAX_GENERATION_TRIES = 1000
    }
}
