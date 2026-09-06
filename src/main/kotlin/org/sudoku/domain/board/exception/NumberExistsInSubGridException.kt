package org.sudoku.domain.board.exception

import org.sudoku.domain.violation.Violation

class NumberExistsInSubGridException(violation: Violation) : ViolationException(violation)