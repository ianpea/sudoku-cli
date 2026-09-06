package org.sudoku.domain.board.exception

import org.sudoku.common.SudokuException
import org.sudoku.domain.violation.Violation

abstract class ViolationException(val violation: Violation) : SudokuException(violation.message())
