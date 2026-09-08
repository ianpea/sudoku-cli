package org.sudoku

import org.junit.jupiter.api.Test
import org.sudoku.domain.violation.ViolationTracker
import org.sudoku.domain.violation.ViolationType
import org.sudoku.fixtures.SudokuFixtures
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ViolationTrackerTest {
    private val violationTracker = ViolationTracker()

    @Test
    fun `findViolation detects row violation`() {
        val board = SudokuFixtures.rowViolationBoard()

        val violation = violationTracker.findViolation(
            board.getRows(),
            board.getColumns(),
            board.getSubgrids()
        )

        assertNotNull(violation)
        assertEquals(ViolationType.ROW, violation.type)
        assertEquals(0, violation.index)
        assertEquals(5, violation.value)
    }

    @Test
    fun `findViolation detects column violation`() {
        val board = SudokuFixtures.columnViolationBoard()

        val violation = violationTracker.findViolation(
            board.getRows(),
            board.getColumns(),
            board.getSubgrids()
        )

        assertNotNull(violation)
        assertEquals(ViolationType.COLUMN, violation.type)
        assertEquals(0, violation.index)
        assertEquals(5, violation.value)
    }

    @Test
    fun `findViolation detects subgrid violation`() {
        val board = SudokuFixtures.subgridViolationBoard()

        val violation = violationTracker.findViolation(
            board.getRows(),
            board.getColumns(),
            board.getSubgrids()
        )

        assertNotNull(violation)
        assertEquals(ViolationType.SUBGRID, violation.type)
        assertEquals(0, violation.index)
        assertEquals(5, violation.value)
    }

    @Test
    fun `returns no violation for valid board`() {
        val board = SudokuFixtures.noViolationBoard()

        val violation = violationTracker.findViolation(
            board.getRows(),
            board.getColumns(),
            board.getSubgrids()
        )

        assertNull(violation)
    }

    @Test
    fun `ignores empty cells`() {
        val board = SudokuFixtures.zerosBoard()

        val violation = violationTracker.findViolation(
            board.getRows(),
            board.getColumns(),
            board.getSubgrids()
        )

        assertNull(violation)
    }
}