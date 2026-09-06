package org.sudoku.cli.input

class ConsoleGameIO: GameIO {
    override fun read(): String = readln()
    override fun print(message: Any) {
        kotlin.io.print(message)
    }
    override fun println(message: Any) {
        kotlin.io.println(message)
    }
}