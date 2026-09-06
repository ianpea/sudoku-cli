package org.sudoku.app

import org.sudoku.cli.exception.InvalidInputException
import org.sudoku.cli.input.CheckCommand
import org.sudoku.cli.input.ClearCommand
import org.sudoku.cli.input.CommandParser
import org.sudoku.cli.input.ExitCommand
import org.sudoku.cli.input.HintCommand
import org.sudoku.cli.input.InsertCommand
import org.sudoku.cli.input.ShowLastMoveCommand
import org.sudoku.cli.input.UndoCommand
import org.sudoku.domain.cell.CellPosition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CommandParserTest {
    private val parser = CommandParser('I')

    @Test
    fun `parser accepts commands case-insensitively`() {
        assertEquals(ExitCommand, parser.parse("quit"))
        assertEquals(ExitCommand, parser.parse("QuiT"))
        assertEquals(ExitCommand, parser.parse("exIt"))

        assertEquals(CheckCommand, parser.parse("check"))

        assertEquals(HintCommand, parser.parse("hint"))

        assertEquals(UndoCommand, parser.parse("undo"))

        assertEquals(ShowLastMoveCommand, parser.parse("lastmove"))
    }

    @Test
    fun `parser converts insert and clear coordinates to zero-based positions`() {
        assertEquals(InsertCommand(CellPosition(2, 4), 7), parser.parse("C5 7"))
        assertEquals(ClearCommand(CellPosition(2, 4)), parser.parse("C5 clear"))
    }

    @Test
    fun `parser rejects malformed or out of range input`() {
        listOf("A 1", "A1 2 3", "a 1", "a1", "1 1", "clear j1", "j1 clear", "undo something").forEach { input ->
            assertFailsWith<InvalidInputException> { parser.parse(input) }
        }
    }

    @Test
    fun `parse rejects malformed clear command`() {
        listOf("A1 CLEARasd").forEach { input ->
            assertFailsWith<InvalidInputException> { parser.parse(input) }
        }
    }
}