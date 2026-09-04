package org.sudoku.domain.cell

data class Cell(var position: CellPosition, var type: CellType = CellType.PRE_FILLED, var value : Int = 0, var solution: Int = 0) {

}

data class CellPosition(var row: Int, var col: Int){
    fun toCoordinateString(): String =
        "${'A' + row}${col + 1}"
}

enum class CellType {
    PRE_FILLED,
    FILLABLE
}
