package org.sudoku.cli.status

import org.sudoku.common.result.DomainResult

data class DomainStatus(val result: DomainResult): Status{
    override val message: String
            get() = result.message
}