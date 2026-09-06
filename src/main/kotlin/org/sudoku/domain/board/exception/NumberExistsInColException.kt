package org.sudoku.domain.board.exception

import org.sudoku.domain.violation.Violation

class NumberExistsInColException(violation: Violation) : ViolationException(violation)