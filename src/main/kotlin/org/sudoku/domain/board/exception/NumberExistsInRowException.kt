package org.sudoku.domain.board.exception

import org.sudoku.domain.violation.Violation

class NumberExistsInRowException(violation: Violation) : ViolationException(violation)