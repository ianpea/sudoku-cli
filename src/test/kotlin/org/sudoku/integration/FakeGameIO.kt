package org.sudoku.integration

import org.sudoku.cli.input.GameIO

class FakeGameIO(
    inputs: List<String>
) : GameIO {

    private val inputs = inputs.iterator()
    private val output = StringBuilder()

    override fun read(): String = inputs.next()

    override fun print(message: Any) {
        output.append(message)
    }

    override fun println(message: Any) {
        output.appendLine(message)
    }

    fun output(): String = output.toString()
}