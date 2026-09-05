package org.sudoku.common.status

data class ErrorStatus(val errorMessage: String? = DEFAULT_MESSAGE) : GameStatus {
    override val message: String
        get() = errorMessage ?: DEFAULT_MESSAGE

    companion object {
        const val DEFAULT_MESSAGE = "An unexpected error has occurred."
    }
}
