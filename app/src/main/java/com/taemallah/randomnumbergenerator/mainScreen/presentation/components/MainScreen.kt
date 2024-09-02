package com.taemallah.randomnumbergenerator.mainScreen.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taemallah.randomnumbergenerator.R
import com.taemallah.randomnumbergenerator.mainScreen.presentation.GeneratorState
import com.taemallah.randomnumbergenerator.mainScreen.presentation.MainEvent
import com.taemallah.randomnumbergenerator.mainScreen.presentation.MainState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(state: MainState, onEvent: (event:MainEvent)->Unit) {
    Scaffold {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(R.string.random_numbers_generator),
                style = MaterialTheme.typography.titleLarge
            )
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth(.8f)
            ) {
                SegmentedButton(
                    selected = state.isGeneratingNormal,
                    onClick = { onEvent(MainEvent.SetIsNormal(true)) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) {
                    Text(text = stringResource(R.string.normal_number))
                }
                SegmentedButton(
                    selected = !state.isGeneratingNormal,
                    onClick = { onEvent(MainEvent.SetIsNormal(false)) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) {
                    Text(text = stringResource(R.string.decimal_number))
                }
            }
            Column (
                modifier = Modifier.fillMaxWidth().animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                OutlinedTextField(
                    value = state.minValue,
                    onValueChange = {onEvent(MainEvent.SetMinValue(it))},
                    label = { Text(text = stringResource(R.string.minimum))},
                    isError = state.isErrorOccurred.isErrorOccurred,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = state.maxValue,
                    onValueChange = {onEvent(MainEvent.SetMaxValue(it))},
                    label = { Text(text = stringResource(R.string.maximum_exclusive))},
                    isError = state.isErrorOccurred.isErrorOccurred,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
                )
                AnimatedVisibility(visible = state.isErrorOccurred.isErrorOccurred) {
                    Text(
                        text = state.isErrorOccurred.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            AnimatedContent(targetState = state.generatorState, label = "") {
                when(it){
                    GeneratorState.Empty -> {
                        Text(text = "?", style = MaterialTheme.typography.displaySmall)
                    }
                    GeneratorState.Generating -> {
                        CircularProgressIndicator()
                    }
                    GeneratorState.Filled -> {
                        Text(
                            text = state.generated,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .animateContentSize()
                        )
                    }
                }
            }
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Button(onClick = { onEvent(MainEvent.GenerateValue) }) {
                    Text(text = stringResource(R.string.generate_a_number))
                }
                OutlinedButton(onClick = { onEvent(MainEvent.GenerateMultiple(10)) }) {
                    Text(text = stringResource(R.string.generate_10))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(state = MainState()) {

    }
}