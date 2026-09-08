package org.sudoku

import org.sudoku.app.GameService
import org.sudoku.app.MoveService
import org.sudoku.cli.input.CheckCommand
import org.sudoku.cli.input.ClearCommand
import org.sudoku.cli.input.CommandParser
import org.sudoku.cli.input.ExitCommand
import org.sudoku.cli.input.HintCommand
import org.sudoku.cli.input.InsertCommand
import org.sudoku.cli.exception.ParserException
import org.sudoku.cli.Renderer
import org.sudoku.cli.input.ConsoleGameIO
import org.sudoku.cli.input.GameIO
import org.sudoku.cli.input.ShowLastMoveCommand
import org.sudoku.cli.input.ShowViolationsCommand
import org.sudoku.cli.input.UndoCommand
import org.sudoku.common.status.ErrorStatus
import org.sudoku.common.status.SessionEndedGameStatus
import org.sudoku.common.status.GameStatus
import org.sudoku.domain.board.Board
import org.sudoku.common.SudokuException
import org.sudoku.common.status.CompletedGameStatus
import org.sudoku.domain.board.BoardGenerator
import org.sudoku.domain.board.PuzzleGenerator
import org.sudoku.domain.violation.ViolationTracker


fun main() {
    val io = ConsoleGameIO()
    runGame(io)
}

fun runGame(io: GameIO, puzzleGenerator: BoardGenerator = PuzzleGenerator()) {
    // Game instantiation
    val moveService = MoveService()
    val violationTracker = ViolationTracker()
    val gameService = GameService(moveService, puzzleGenerator, violationTracker)
    gameService.startGame()
    val renderer = Renderer(gameService.board.cells, io)
    val commandParser = CommandParser(Board.MAX_ROW_ALPHABET)

    var gameStatusMessage: GameStatus?

    // Game start
    renderer.renderWelcomeMessage()
    renderer.render(null)

    // Game loop
    while (gameService.checkWinCondition() !is CompletedGameStatus) {
        val input = io.read()

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
                    return
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

                ShowViolationsCommand -> {
                    gameService.check()
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

    val gameStatus = gameService.checkWinCondition()
    renderer.render(gameStatus)
}