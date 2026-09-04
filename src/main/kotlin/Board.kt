package org.sudoku

import org.sudoku.exceptions.NoHintLeftException
import org.sudoku.exceptions.board.InsertToPreFilledCellException
import org.sudoku.exceptions.board.InvalidCellValueException
import kotlin.math.sqrt

class Board(val size: Int = 9) {
    init {
        require(size in 2..9) {
            // TODO
            "Board size must be between 2 and 9."
        }
    }


    val cellCount: Int
        get() = size * size
    val boxSize: Int
        get() = sqrt(size.toDouble()).toInt()
    val cells: List<Cell> = List(cellCount) { index ->
        Cell(CellPosition(index / size, index % size))
    }
    val MAX_ROW = 'A' + (size - 1)

    lateinit var solution: List<Cell>


    /**
     * Check whether given value can be filled into the cell.
     * @param self When true, checks the cell itself whether its value is truthy in a board or not
     */
    fun check(cell: Cell, value: Int, self: Boolean = false): Boolean {
        val row = cell.position.row
        val col = cell.position.col
        val index = row * size + col

        val hypotheticalBoard = cells.toMutableList()
        if (hypotheticalBoard[row * size + col].value != 0 && !self) {
//            println("cell already occupied")
            return false
        }

        // when checking if the value is correct, just replace it and check it again

        if (self) {
            hypotheticalBoard[index].value = 0
        }

        val hypotheticalRow = hypotheticalBoard.filter { cell -> cell.position.row == row }.map { cell -> cell.value }
        val hypotheticalCol = hypotheticalBoard.filter { cell -> cell.position.col == col }.map { cell -> cell.value }


        // row check
        if (hypotheticalRow.contains(value)) {
//            println("value already exists in the row, ${hypotheticalRow.contentToString()}")
            return false // value already exists in the row
        }

        // column check
        if (hypotheticalCol.contains(value)) {
//            println("value already exists in the row")
            return false // value already exists in the row
        }

        // box check
        val hypotheticalBox = getBox(cell.position)
        if (hypotheticalBox.contains(value)) {
            return false
        }

        return true
    }

    /**
     *   Insert the value into the cell, if its not pre-filled.
     *   @param smart Flag to determine whether user input is checked before filling in the cell. Defaults to false
     */
    fun insert(row: Int, col: Int, valueToBe: Int, smart: Boolean = false): String {
        val index = row * size + col
        val cell = cells[index]

        if (cell.type == CellType.PRE_FILLED) {
            // requirement #1 Invalid move
            throw InsertToPreFilledCellException(cell, valueToBe)
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
            return "Successfully inserted to ${'A' + (row + 1)}$col$valueToBe."
        }
    }

    fun fillBoard(index: Int = 0): Boolean {
        if (index == cellCount) return true
        val cell = cells[index]
//        println("filling board")
        for (value in (1..size).shuffled()) {
            if (check(cell, value)) {
                cells[index].value = value
                cells[index].solution = value
                if (fillBoard(index + 1)) {
                    return true
                }
                cells[index].value = 0
                cells[index].solution = 0
            }
        }

        return false
    }

    fun generatePuzzle(expectedClueCount: Int = 30) {
        // With a full board use backtrack to generate a 1 unique solution board.
        var currentClueCount = cellCount
        for (i in (0 until cellCount).shuffled()) {
            if (currentClueCount <= expectedClueCount) {
                break
            }

            val originalValue = cells[i].value

            cells[i].value = 0
            cells[i].type = CellType.FILLABLE
            if (countSolution() == 1) {
                currentClueCount--
            } else {
                cells[i].value = originalValue
                cells[i].type = CellType.PRE_FILLED
            }
        }


        if (currentClueCount != expectedClueCount) {
            throw IllegalStateException(
                "Could not generate puzzle with exactly $expectedClueCount clues."
            )
        }
        // If it reaches here, a board with unique result has been generated.
        solution = cells.map { it.copy() }
    }

    fun getBox(position: CellPosition): IntArray {
//        (7,4)
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

    fun countSolution(index: Int = 0, limit: Int = MAX_SOLUTION_COUNT): Int {
        // Reached past the last cell:
        // this branch produced one complete valid solution.
        if (index == cellCount) {
            return 1
        }

        val cell = cells[index]
        var solutionCount = 0

        // This cell is already fixed, so continue to the next cell.
        if (cells[index].value != 0) {
            return countSolution(index + 1)
        }

        // All your multiverse starts here:
        // each valid value creates a different possible branch.
        for (value in (1..size).shuffled()) {
            if (check(cell, value)) {
                cells[index].value = value

                val resultFromChild = countSolution(index + 1, limit - solutionCount)
                solutionCount += resultFromChild
            }

            // Undo this choice before trying another branch.
            cells[index].value = 0

            // Stop once this call has found enough solutions
            // to satisfy the requested limit.
            if (solutionCount >= limit) {
                return solutionCount
            }
        }
        return solutionCount
    }

    fun checkWinCondition(): Boolean {
        for (i in 0 until cellCount) {
            if (!check(cells[i], cells[i].value, self = true)) {
                return false
            }
        }
        return true
    }

    fun hint(): String {
        val emptyCells = cells.filter({ it.type == CellType.FILLABLE && it.value == 0 })
        val hint = emptyCells.randomOrNull()
        if (hint != null) {
            return "Hint: ${'A' + (hint.position.row)}${hint.position.col + 1} ${hint.solution}"
        }

        throw NoHintLeftException()
    }
    companion object {
        const val MAX_SOLUTION_COUNT = 2
    }
}
