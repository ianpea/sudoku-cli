package org.sudoku.common.status

import org.sudoku.common.result.DomainResult

data class DomainStatus(val result: DomainResult): GameStatus{
    override val message: String
            get() = result.message
}