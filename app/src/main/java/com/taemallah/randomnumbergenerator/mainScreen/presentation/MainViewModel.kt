package com.taemallah.randomnumbergenerator.mainScreen.presentation

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taemallah.randomnumbergenerator.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),MainState())

    fun onEvent(event: MainEvent){
        when(event){
            is MainEvent.GenerateMultiple -> {
                if (_state.value.isErrorOccurred.isErrorOccurred) {
                    Toast.makeText(application,
                        application.getString(R.string.error_occurred_please_check_your_inputs),Toast.LENGTH_LONG).show()
                    return
                }
                var value = ""
                if (_state.value.isGeneratingNormal){
                    for (i in 1..event.count){
                        value += Random.nextLong(toLong(_state.value.minValue),toLong(_state.value.maxValue)).toString() + "\n"
                    }
                }else{
                    for (i in 1..event.count){
                        value += Random.nextDouble(toDouble(_state.value.minValue),toDouble(_state.value.maxValue)).toString() + "\n"
                    }
                }
                _state.update {
                    it.copy(
                        generated = value.trim(),
                        generatorState = GeneratorState.Filled
                    )
                }
            }
            MainEvent.GenerateValue -> {
                if (_state.value.isErrorOccurred.isErrorOccurred) {
                    Toast.makeText(application,
                        application.getString(R.string.error_occurred_please_check_your_inputs),Toast.LENGTH_LONG).show()
                    return
                }
                val value: String
                if (_state.value.isGeneratingNormal){
                    value = Random.nextLong(toLong(_state.value.minValue),toLong(_state.value.maxValue)).toString()
                }else{
                    value = Random.nextDouble(toDouble(_state.value.minValue),toDouble(_state.value.maxValue)).toString()
                }
                _state.update {
                    it.copy(
                        generated = value,
                        generatorState = GeneratorState.Filled
                    )
                }
            }
            is MainEvent.SetIsNormal -> {
                if (event.isNormalNumber){
                    _state.update {
                        it.copy(
                            isGeneratingNormal = true,
                            generatorState = GeneratorState.Empty)
                    }
                    onEvent(MainEvent.SetMinValue(toDouble(_state.value.minValue).toLong().toString()))
                    onEvent(MainEvent.SetMaxValue(toDouble(_state.value.maxValue).toLong().toString()))
                }else{
                    _state.update {
                        it.copy(
                            isGeneratingNormal = false,
                            generatorState = GeneratorState.Empty)
                    }
                }
            }
            is MainEvent.SetMaxValue -> {
                try {
                    if (_state.value.isGeneratingNormal){
                        toLong(event.maxValue).toString()
                    }else{
                        toDouble(event.maxValue)
                    }
                }catch (_: Exception){
                    return
                }
                _state.update {
                    it.copy(
                        maxValue = event.maxValue,
                    )
                }
                if (toDouble(_state.value.minValue)>=toDouble(_state.value.maxValue)){
                    _state.update {
                        it.copy(
                            isErrorOccurred = GeneratorError().withError(application.getString(R.string.min_value_should_be_less_than_max_value))
                        )
                    }
                }else{
                    _state.update {
                        it.copy(
                            isErrorOccurred = GeneratorError().noError()
                        )
                    }
                }
            }
            is MainEvent.SetMinValue -> {
                try {
                    if (_state.value.isGeneratingNormal){
                        toLong(event.minValue).toString()
                    }else{
                        toDouble(event.minValue)
                    }
                }catch (_: Exception){
                    return
                }
                _state.update {
                    it.copy(
                        minValue = event.minValue,
                    )
                }
                if (toDouble(_state.value.minValue)>=toDouble(_state.value.maxValue)){
                    _state.update {
                        it.copy(
                            isErrorOccurred = GeneratorError().withError(application.getString(R.string.min_value_should_be_less_than_max_value))
                        )
                    }
                }else{
                    _state.update {
                        it.copy(
                            isErrorOccurred = GeneratorError().noError()
                        )
                    }
                }
            }
        }
    }

    private fun toLong(value: String): Long{
        return if (value.isBlank()) 0 else value.toLong()
    }

    private fun toDouble(value: String): Double{
        return if (value.isBlank()) 0.0 else value.toDouble()
    }
}