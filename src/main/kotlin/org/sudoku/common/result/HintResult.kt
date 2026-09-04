package org.sudoku.common.result

import org.sudoku.domain.cell.Cell

data class HintResult(
    val hint: Cell
) : DomainResult {
    override val message: String = "Hint: ${'A' + (hint.position.row)}${hint.position.col + 1} ${hint.solution}"
}