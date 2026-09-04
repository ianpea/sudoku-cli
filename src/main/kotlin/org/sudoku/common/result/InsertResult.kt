package org.sudoku.common.result

import org.sudoku.domain.cell.Cell

data class InsertResult(
    val cell: Cell
) : DomainResult {
    override val message: String = "Inserted ${cell.value} to ${'A' + cell.position.row}${cell.position.col+1}."
}
