package org.sudoku

data class Cell(var position: CellPosition, var type: CellType = CellType.PRE_FILLED, var value : Int = 0, var solution: Int = 0) {

}

data class CellPosition(var row: Int, var col: Int)

enum class CellType {
    PRE_FILLED,
    FILLABLE
}
