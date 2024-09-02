package com.taemallah.randomnumbergenerator.mainScreen.presentation

data class MainState(
    val minValue: String = "1",
    val maxValue: String = "100",
    val generated: String = "?",
    val isGeneratingNormal: Boolean = true,
    val isErrorOccurred: GeneratorError = GeneratorError(),
    val generatorState: GeneratorState = GeneratorState.Empty,
)