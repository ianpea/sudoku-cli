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
import org.sudoku.cli.status.BoardClearedStatus
import org.sudoku.cli.status.CompletedStatus
import org.sudoku.cli.status.DomainStatus
import org.sudoku.cli.status.ErrorStatus
import org.sudoku.cli.status.NotCompletedStatus
import org.sudoku.cli.status.SessionEndedStatus
import org.sudoku.cli.status.Status
import org.sudoku.domain.board.exception.SudokuException


fun main() {
    val gameService = GameService()
    val renderer = Renderer()


    val board = gameService.startGame()

    val commandParser = CommandParser(board.maxRowAlphabet)

    var statusMessage: Status?

    renderer.renderWelcomeMessage(board)
    renderer.render(board, null)

    // Main game loop
    while (true) {
        val input = readln().trim()

        try {
            val command = commandParser.parse(input)
            statusMessage = when (command) {
                CheckCommand -> {
                    val win = gameService.check()
                    // TODO add moves history
                    if (win) {
                        renderer.render(CompletedStatus(1))
                        break
                    } else {
                        NotCompletedStatus()
                    }
                }

                ClearCommand -> {
                    gameService.clear()
                    BoardClearedStatus()
                }

                ExitCommand -> {
                    renderer.render(SessionEndedStatus())
                    break
                }

                HintCommand -> {
                    DomainStatus(gameService.hint())
                }

                is InsertCommand -> {
                    DomainStatus(gameService.insert(command.cell, command.value))
                }
            }
        } catch (e: SudokuException) {
            statusMessage = ErrorStatus(e.message)
        } catch (e: ParserException){
            statusMessage = ErrorStatus(e.message)
        }

        if(statusMessage is ErrorStatus){
            renderer.render(statusMessage)
        }else{
            renderer.render(board, statusMessage)
        }
    }
}