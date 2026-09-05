package org.sudoku

import org.sudoku.app.GameService
import org.sudoku.app.MoveService
import org.sudoku.cli.CheckCommand
import org.sudoku.cli.ClearCommand
import org.sudoku.cli.CommandParser
import org.sudoku.cli.ExitCommand
import org.sudoku.cli.HintCommand
import org.sudoku.cli.InsertCommand
import org.sudoku.cli.exception.ParserException
import org.sudoku.cli.Renderer
import org.sudoku.cli.ShowLastMoveCommand
import org.sudoku.cli.UndoCommand
import org.sudoku.common.status.ErrorStatus
import org.sudoku.common.status.SessionEndedGameStatus
import org.sudoku.common.status.GameStatus
import org.sudoku.domain.board.Board
import org.sudoku.common.SudokuException


fun main() {
    // Game instantiation
    val moveService = MoveService()
    val gameService = GameService(moveService, 79)
    gameService.startGame()
    val renderer = Renderer(gameService.board)
    val commandParser = CommandParser(Board.MAX_ROW_ALPHABET)

    var gameStatusMessage: GameStatus?

    // Game start
    renderer.renderWelcomeMessage()
    renderer.render(null)

    // Game loop
    while (true) {
        val input = readln().trim()

        try {
            val command = commandParser.parse(input)
            gameStatusMessage = when (command) {

                is InsertCommand -> {
                    gameService.insert(command)
                }

                is ClearCommand -> {
                    gameService.clear(command)
                }

                CheckCommand -> {
                    gameService.check()
                }

                ExitCommand -> {
                    // Game end
                    renderer.renderStatusOnly(SessionEndedGameStatus())
                    break
                }

                HintCommand -> {
                    gameService.hint()
                }

                UndoCommand -> {
                    gameService.undo()
                }

                ShowLastMoveCommand -> {
                    moveService.showLastMove()
                }

            }
        } catch (e: SudokuException) {
            gameStatusMessage = ErrorStatus(e.message)
        } catch (e: ParserException) {
            gameStatusMessage = ErrorStatus(e.message)
        }

        if (gameStatusMessage is ErrorStatus) {
            renderer.renderStatusOnly(gameStatusMessage)
        } else {
            renderer.render(gameStatusMessage)
        }
    }
}