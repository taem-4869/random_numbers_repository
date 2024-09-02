package com.taemallah.randomnumbergenerator.mainScreen.presentation

data class GeneratorError(
    var isErrorOccurred : Boolean = false,
    var errorMessage: String = ""
){
    fun noError(): GeneratorError{
        return this.copy(
            isErrorOccurred = false,
            errorMessage = ""
        )
    }
    fun withError(errorMessage: String): GeneratorError{
        return this.copy(
            isErrorOccurred = true,
            errorMessage = errorMessage
        )
    }
}