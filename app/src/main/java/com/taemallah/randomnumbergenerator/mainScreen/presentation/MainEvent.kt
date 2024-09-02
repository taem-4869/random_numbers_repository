package com.taemallah.randomnumbergenerator.mainScreen.presentation

sealed class MainEvent {
    data class SetMinValue(val minValue: String): MainEvent()
    data class SetMaxValue(val maxValue: String): MainEvent()
    data class SetIsNormal(val isNormalNumber: Boolean): MainEvent()
    data class GenerateMultiple(val count: Int): MainEvent()
    data object GenerateValue: MainEvent()
}