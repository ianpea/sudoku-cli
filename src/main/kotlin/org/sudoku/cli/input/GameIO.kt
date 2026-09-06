package org.sudoku.cli.input

interface GameIO {
    fun read(): String
    fun println(message: Any = "")
    fun print(message: Any = "")
}