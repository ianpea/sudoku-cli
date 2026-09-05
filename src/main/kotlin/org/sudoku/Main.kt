package org.sudoku

import org.sudoku.app.GameService
import org.sudoku.cli.CheckCommand
import org.sudoku.cli.ClearCommand
import org.sudoku.cli.CommandParser
import org.sudoku.cli.ExitCommand
import org.sudoku.cli.HintCommand
import org.sudoku.cli.InsertCommand
import org.sudoku.cli.ParserException
import org.sudoku.cli.Renderer
import org.sudoku.common.status.CellClearedStatus
import org.sudoku.common.status.DomainStatus
import org.sudoku.common.status.ErrorStatus
import org.sudoku.common.status.SessionEndedStatus
import org.sudoku.common.status.Status
import org.sudoku.domain.board.Board
import org.sudoku.domain.board.exception.SudokuException


fun main() {
    // Game prep
    val gameService = GameService(75)
    val renderer = Renderer()
    val board = gameService.startGame()
    val commandParser = CommandParser(Board.MAX_ROW_ALPHABET)

    var statusMessage: Status?

    // Game start
    renderer.renderWelcomeMessage()
    renderer.render(board, null)

    // Game loop
    while (true) {
        val input = readln().trim()

        try {
            val command = commandParser.parse(input)
            statusMessage = when (command) {

                is InsertCommand -> {
                    DomainStatus(board.insert(command.cell, command.value))
                }

                is ClearCommand -> {
                    gameService.clear(command.cell)
                    CellClearedStatus(command.cell)
                }

                CheckCommand -> {
                    board.checkWinStatus()
                }


                ExitCommand -> {
                    // Game end
                    renderer.render(SessionEndedStatus())
                    break
                }

                HintCommand -> {
                    DomainStatus(board.hint())
                }
            }
        } catch (e: SudokuException) {
            statusMessage = ErrorStatus(e.message)
        } catch (e: ParserException) {
            statusMessage = ErrorStatus(e.message)
        }

        if (statusMessage is ErrorStatus) {
            renderer.render(statusMessage)
        } else {
            renderer.render(board, statusMessage)
        }
    }
}