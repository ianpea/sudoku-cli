package org.sudoku.domain.cell

import org.sudoku.domain.cell.exception.CannotClearEmptyCellException
import org.sudoku.domain.cell.exception.CannotClearPreFilledCellException
import org.sudoku.domain.cell.exception.CannotInsertPreFilledCellException

data class Cell(
    var position: CellPosition,
    var type: CellType = CellType.PRE_FILLED,
    var value: Int = 0,
    var solution: Int = 0
) {

    fun clear() {
        if (type == CellType.FILLABLE) {
            if (value != 0) {
                value = 0

            } else {
                throw CannotClearEmptyCellException(position)
            }
        } else {
            throw CannotClearPreFilledCellException(position)
        }
    }

    fun insert(value: Int) {
        if (type == CellType.FILLABLE) {
            this.value = value
        } else {
            throw CannotInsertPreFilledCellException(position)
        }
    }
}

data class CellPosition(var row: Int, var col: Int) {
    fun toCoordinateString(): String =
        "${'A' + row}${col + 1}"

    fun toUserRow(): Char = 'A' + row
    fun toUserCol(): Int = col + 1

}

enum class CellType {
    PRE_FILLED,
    FILLABLE
}
